package com.larryyu.presentation.viewmodel
import com.larryyu.domain.entity.BaseResponse
import com.larryyu.domain.model.AgentDetailsData
import com.larryyu.domain.model.AgentsModel
import com.larryyu.domain.model.Role
import com.larryyu.domain.repository.AgentsRepo
import com.larryyu.domain.utils.DataState
import com.larryyu.presentation.uistates.AgentsIntent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
import kotlin.test.*
@OptIn(ExperimentalCoroutinesApi::class)
class AgentsViewModelTest {
    private lateinit var viewModel: AgentsViewModel
    private lateinit var mockAgentsRepo: FakeAgentsRepository
    private val testDispatcher = StandardTestDispatcher()
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockAgentsRepo = FakeAgentsRepository()
        viewModel = AgentsViewModel(mockAgentsRepo)
    }
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }
    @Test
    fun `initial state should be loading false with empty list`() = runTest {
        val initialState = viewModel.agentsState.value
        assertFalse(initialState.isLoading, "Initial loading state should be false")
        assertTrue(initialState.agents.isEmpty(), "Initial agents list should be empty")
        assertNull(initialState.error, "Initial error should be null")
    }
    @Test
    fun `fetch agents intent should load agents successfully`() = runTest {
        val mockAgents = listOf(
            createMockAgent("1", "Jett"),
            createMockAgent("2", "Phoenix")
        )
        mockAgentsRepo.setAgentsResult(DataState.Success(BaseResponse(data = mockAgents)))
        viewModel.sendIntent(AgentsIntent.FetchAgents)
        advanceUntilIdle()
        val state = viewModel.agentsState.value
        assertFalse(state.isLoading, "Loading should be false after success")
        assertEquals(2, state.agents.size, "Should have 2 agents")
        assertEquals("Jett", state.agents[0].displayName, "First agent should be Jett")
        assertNull(state.error, "Error should be null on success")
    }
    @Test
    fun `fetch agents should show loading state during fetch`() = runTest {
        mockAgentsRepo.setAgentsResult(DataState.Loading)
        viewModel.sendIntent(AgentsIntent.FetchAgents)
        advanceTimeBy(100)
        val state = viewModel.agentsState.value
        assertTrue(state.isLoading, "Loading should be true during fetch")
    }
    @Test
    fun `fetch agents should handle error state`() = runTest {
        val errorMessage = "Network error"
        mockAgentsRepo.setAgentsResult(DataState.Error(Exception(errorMessage)))
        viewModel.sendIntent(AgentsIntent.FetchAgents)
        advanceUntilIdle()
        val state = viewModel.agentsState.value
        assertFalse(state.isLoading, "Loading should be false after error")
        assertNotNull(state.error, "Error message should not be null")
        assertTrue(state.agents.isEmpty(), "Agents list should remain empty on error")
    }
    @Test
    fun `refresh agents intent should reload agents`() = runTest {
        val initialAgents = listOf(createMockAgent("1", "Jett"))
        mockAgentsRepo.setAgentsResult(DataState.Success(BaseResponse(data = initialAgents)))
        viewModel.sendIntent(AgentsIntent.FetchAgents)
        advanceUntilIdle()
        val refreshedAgents = listOf(
            createMockAgent("1", "Jett"),
            createMockAgent("2", "Phoenix")
        )
        mockAgentsRepo.setAgentsResult(DataState.Success(BaseResponse(data = refreshedAgents)))
        viewModel.sendIntent(AgentsIntent.RefreshAgents)
        advanceUntilIdle()
        val state = viewModel.agentsState.value
        assertEquals(2, state.agents.size, "Should have refreshed agents")
        assertFalse(state.isLoading, "Loading should be complete")
    }
    @Test
    fun `multiple fetch requests should handle sequentially`() = runTest {
        val mockAgents = listOf(createMockAgent("1", "Jett"))
        mockAgentsRepo.setAgentsResult(DataState.Success(BaseResponse(data = mockAgents)))
        viewModel.sendIntent(AgentsIntent.FetchAgents)
        viewModel.sendIntent(AgentsIntent.FetchAgents)
        viewModel.sendIntent(AgentsIntent.FetchAgents)
        advanceUntilIdle()
        val state = viewModel.agentsState.value
        assertFalse(state.isLoading)
        assertEquals(1, state.agents.size)
    }
    @Test
    fun `open details intent should navigate to agent details`() = runTest {
        val agentId = "test-agent-id-123"
        viewModel.sendIntent(AgentsIntent.OpenDetails(agentId))
        advanceUntilIdle()
    }
    @Test
    fun `loading cached agents should populate state immediately`() = runTest {
        val cachedAgents = listOf(
            createMockAgent("1", "Jett"),
            createMockAgent("2", "Phoenix"),
            createMockAgent("3", "Sage")
        )
        mockAgentsRepo.setCachedAgents(cachedAgents)
        viewModel.sendIntent(AgentsIntent.FetchAgents)
        advanceUntilIdle()
        val state = viewModel.agentsState.value
        assertEquals(3, state.agents.size, "Should load all cached agents")
        assertTrue(
            state.agents.any { it.displayName == "Sage" },
            "Should contain Sage"
        )
    }
    @Test
    fun `error state should clear on successful retry`() = runTest {
        mockAgentsRepo.setAgentsResult(DataState.Error(Exception("Network error")))
        viewModel.sendIntent(AgentsIntent.FetchAgents)
        advanceUntilIdle()
        val errorState = viewModel.agentsState.value
        assertNotNull(errorState.error, "Should have error initially")
        mockAgentsRepo.setAgentsResult(
            DataState.Success(BaseResponse(data = listOf(createMockAgent("1", "Jett"))))
        )
        viewModel.sendIntent(AgentsIntent.RefreshAgents)
        advanceUntilIdle()
        val successState = viewModel.agentsState.value
        assertNull(successState.error, "Error should be cleared")
        assertEquals(1, successState.agents.size, "Should have agents")
    }
    private fun createMockAgent(uuid: String, displayName: String) = AgentsModel(
        uuid = uuid,
        displayName = displayName,
        fullPortrait = "https://example.com/portrait.png",
        role = Role(displayName = "Duelist"),
        fullPortraitV2 = "https://example.com/portraitv2.png"
    )
}
class FakeAgentsRepository : AgentsRepo {
    private var agentsResult: DataState<BaseResponse<List<AgentsModel>>> =
        DataState.Success(BaseResponse(data = emptyList()))
    private var cachedAgents: List<AgentsModel> = emptyList()
    fun setAgentsResult(result: DataState<BaseResponse<List<AgentsModel>>>) {
        agentsResult = result
    }
    fun setCachedAgents(agents: List<AgentsModel>) {
        cachedAgents = agents
        agentsResult = DataState.Success(BaseResponse(data = agents))
    }
    override suspend fun getAgents(): Flow<DataState<BaseResponse<List<AgentsModel>>>> = flow {
        when (val result = agentsResult) {
            is DataState.Loading -> emit(DataState.Loading)
            is DataState.Idle -> emit(DataState.Idle)
            is DataState.Success -> emit(DataState.Success(result.data))
            is DataState.Error -> emit(DataState.Error(result.exception))
        }
    }
    override suspend fun getAgentDetails(id: String): Flow<DataState<BaseResponse<AgentDetailsData>>> = flow {
        emit(DataState.Success(BaseResponse(data = AgentDetailsData())))
    }
    override suspend fun getAgentsFromLocalDatabase(): List<AgentsModel> {
        return cachedAgents
    }
    override suspend fun insertAgentsToLocalDatabase(agents: List<AgentsModel>) {
        cachedAgents = agents
    }
}
