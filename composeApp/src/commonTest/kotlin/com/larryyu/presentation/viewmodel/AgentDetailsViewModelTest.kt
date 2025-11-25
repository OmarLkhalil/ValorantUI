package com.larryyu.presentation.viewmodel

import com.larryyu.domain.entity.BaseResponse
import com.larryyu.domain.model.AbilitiesItemDetails
import com.larryyu.domain.model.AgentDetailsData
import com.larryyu.domain.model.AgentsResponseModel
import com.larryyu.domain.repository.AgentsRepo
import com.larryyu.domain.usecase.AgentDetailsUseCase
import com.larryyu.domain.utils.DataState
import com.larryyu.presentation.uistates.AgentDetailsIntent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class AgentDetailsViewModelTest {
    private lateinit var viewModel: AgentDetailsViewModel
    private lateinit var mockAgentDetailsUseCase: FakeAgentDetailsUseCase
    private lateinit var fakeRepo: FakeAgentsRepoForAgentDetails
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepo = FakeAgentsRepoForAgentDetails()
        mockAgentDetailsUseCase = FakeAgentDetailsUseCase(fakeRepo)
        viewModel = AgentDetailsViewModel(AgentDetailsUseCase(fakeRepo))
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be empty agent details`() = runTest {
        val initialState = viewModel.state.value
        assertFalse(initialState.isLoading, "Initial loading should be false")
        assertTrue(initialState.agentDetails?.agentName?.isEmpty() ?: true, "Display name should be empty")
        assertNull(initialState.error, "Error should be null")
    }

    @Test
    fun `fetch agent details should load successfully`() = runTest {
        val agentId = "test-agent-123"
        val mockAgentDetails = createMockAgentDetails(agentId, "Jett")
        mockAgentDetailsUseCase.setAgentDetailsResult(
            DataState.Success(BaseResponse(data = mockAgentDetails))
        )

        viewModel.sendIntent(AgentDetailsIntent.FetchAgentDetails(agentId))
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading, "Loading should be false after success")
        assertEquals("Jett", state.agentDetails?.agentName, "Agent name should match")
        assertEquals(agentId, state.agentDetails?.agentId, "Agent ID should match")
        assertNull(state.error, "Error should be null on success")
    }

    @Test
    fun `fetch agent details should show loading state`() = runTest {
        mockAgentDetailsUseCase.setAgentDetailsResult(DataState.Loading)

        viewModel.sendIntent(AgentDetailsIntent.FetchAgentDetails("test-agent"))
        advanceTimeBy(100)

        val state = viewModel.state.value
        assertTrue(state.isLoading, "Loading should be true during fetch")
    }

    @Test
    fun `fetch agent details should handle error`() = runTest {
        val errorMessage = "Agent not found"
        mockAgentDetailsUseCase.setAgentDetailsResult(DataState.Error(Exception(errorMessage)))

        viewModel.sendIntent(AgentDetailsIntent.FetchAgentDetails("invalid-id"))
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading, "Loading should be false after error")
        assertNotNull(state.error, "Error should not be null")
    }

    @Test
    fun `fetch different agents should update state correctly`() = runTest {
        val agent1 = createMockAgentDetails("1", "Jett")
        mockAgentDetailsUseCase.setAgentDetailsResult(
            DataState.Success(BaseResponse(data = agent1))
        )

        viewModel.sendIntent(AgentDetailsIntent.FetchAgentDetails("1"))
        advanceUntilIdle()

        val firstState = viewModel.state.value
        assertEquals("Jett", firstState.agentDetails?.agentName)

        val agent2 = createMockAgentDetails("2", "Phoenix")
        mockAgentDetailsUseCase.setAgentDetailsResult(
            DataState.Success(BaseResponse(data = agent2))
        )

        viewModel.sendIntent(AgentDetailsIntent.FetchAgentDetails("2"))
        advanceUntilIdle()

        val secondState = viewModel.state.value
        assertEquals("Phoenix", secondState.agentDetails?.agentName)
        assertNotEquals(firstState.agentDetails?.agentId, secondState.agentDetails?.agentId)
    }

    @Test
    fun `agent details with abilities should load correctly`() = runTest {
        val abilities = listOf(
            createMockAbility("ability1", "Cloudburst", "Q"),
            createMockAbility("ability2", "Updraft", "E"),
            createMockAbility("ability3", "Tailwind", "X"),
            createMockAbility("ability4", "Blade Storm", "C")
        )

        val agent = createMockAgentDetails("jett-id", "Jett", abilities)
        mockAgentDetailsUseCase.setAgentDetailsResult(
            DataState.Success(BaseResponse(data = agent))
        )

        viewModel.sendIntent(AgentDetailsIntent.FetchAgentDetails("jett-id"))
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(4, state.agentDetails?.agentAbilities?.size ?: 0, "Should have 4 abilities")
        assertEquals(
            true,
            state.agentDetails?.agentAbilities?.any { it.abilityName == "Cloudburst" },
            "Should contain Cloudburst ability"
        )
    }

    @Test
    fun `rapid consecutive fetches should handle correctly`() = runTest {
        val agent = createMockAgentDetails("test-id", "Test Agent")
        mockAgentDetailsUseCase.setAgentDetailsResult(
            DataState.Success(BaseResponse(data = agent))
        )

        repeat(5) {
            viewModel.sendIntent(AgentDetailsIntent.FetchAgentDetails("test-id"))
        }

        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals("Test Agent", state.agentDetails?.agentName)
    }

    @Test
    fun `error recovery should work on retry`() = runTest {
        mockAgentDetailsUseCase.setAgentDetailsResult(DataState.Error(Exception("Network timeout")))

        viewModel.sendIntent(AgentDetailsIntent.FetchAgentDetails("test-id"))
        advanceUntilIdle()

        val errorState = viewModel.state.value
        assertNotNull(errorState.error)

        val agent = createMockAgentDetails("test-id", "Recovered Agent")
        mockAgentDetailsUseCase.setAgentDetailsResult(
            DataState.Success(BaseResponse(data = agent))
        )

        viewModel.sendIntent(AgentDetailsIntent.FetchAgentDetails("test-id"))
        advanceUntilIdle()

        val successState = viewModel.state.value
        assertNull(successState.error, "Error should be cleared")
        assertEquals("Recovered Agent", successState.agentDetails?.agentName)
    }

    @Test
    fun `agent details with null abilities should handle gracefully`() = runTest {
        val agent = createMockAgentDetails("test-id", "Agent", null)
        mockAgentDetailsUseCase.setAgentDetailsResult(
            DataState.Success(BaseResponse(data = agent))
        )

        viewModel.sendIntent(AgentDetailsIntent.FetchAgentDetails("test-id"))
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("Agent", state.agentDetails?.agentName)
        assertTrue(state.agentDetails?.agentAbilities.isNullOrEmpty())
    }

    private fun createMockAgentDetails(
        uuid: String,
        agentName: String,
        abilities: List<AbilitiesItemDetails>? = emptyList()
    ) = AgentDetailsData(
        uuid = uuid,
        displayName = agentName,
        description = "Test description for $agentName",
        developerName = "dev-name",
        characterTags = null,
        displayIcon = "https://example.com/icon.png",
        displayIconSmall = "icon-small.png",
        bustPortrait = "bust.png",
        fullPortrait = "https://example.com/portrait.png",
        fullPortraitV2 = "portrait-v2.png",
        killfeedPortrait = "killfeed.png",
        background = "https://example.com/background.png",
        backgroundGradientColors = emptyList(),
        assetPath = "path",
        isFullPortraitRightFacing = true,
        isPlayableCharacter = true,
        isAvailableForTest = true,
        isBaseContent = true,
        role = null,
        recruitmentData = null,
        abilities = abilities,
        voiceLine = null
    )

    private fun createMockAbility(
        @Suppress("UNUSED_PARAMETER") uuid: String,
        abilityName: String,
        slot: String
    ) = AbilitiesItemDetails(
        slot = slot,
        displayName = abilityName,
        description = "Test ability description",
        displayIcon = "https://example.com/ability-icon.png"
    )
}

class FakeAgentDetailsUseCase(
    private val fakeRepo: FakeAgentsRepoForAgentDetails
) {
    fun setAgentDetailsResult(result: DataState<BaseResponse<AgentDetailsData>>) {
        fakeRepo.setAgentDetailsResult(result)
    }
}

class FakeAgentsRepoForAgentDetails : AgentsRepo {
    private var agentDetailsResult: DataState<BaseResponse<AgentDetailsData>> =
        DataState.Success(BaseResponse(data = AgentDetailsData()))

    fun setAgentDetailsResult(result: DataState<BaseResponse<AgentDetailsData>>) {
        agentDetailsResult = result
    }

    override suspend fun getAgents() = flow {
        emit(DataState.Success(BaseResponse(data = emptyList<AgentsResponseModel>())))
    }

    override suspend fun getAgentDetails(id: String) = flow {
        when (val result = agentDetailsResult) {
            is DataState.Loading -> emit(DataState.Loading)
            is DataState.Idle -> emit(DataState.Idle)
            is DataState.Success -> emit(DataState.Success(result.data))
            is DataState.Error -> emit(DataState.Error(result.exception))
        }
    }

    override suspend fun getAgentsFromLocalDatabase() = emptyList<AgentsResponseModel>()
    override suspend fun insertAgentsToLocalDatabase(agents: List<AgentsResponseModel>) {}
}

