package com.larryyu.domain.repository

import com.larryyu.domain.entity.BaseResponse
import com.larryyu.domain.model.AgentDetailsData
import com.larryyu.domain.model.AgentsResponseModel
import com.larryyu.domain.model.AgentsRoleModel
import com.larryyu.domain.utils.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import kotlin.test.*
@OptIn(ExperimentalCoroutinesApi::class)
class AgentsRepositoryTest {
    @Test
    fun `getAgents returns success with valid data`() = runTest {
        val mockRepo = FakeAgentsRepositoryForTest()
        val expectedAgents = listOf(
            createMockAgent("1", "Jett"),
            createMockAgent("2", "Phoenix")
        )
        mockRepo.setAgents(expectedAgents)
        val result = mockRepo.getAgents().first()
        assertTrue(result is DataState.Success)
        val agents = result.data.data
        assertNotNull(agents)
        assertEquals(2, agents.size)
        assertEquals("Jett", agents[0].displayName)
    }
    @Test
    fun `getAgents returns error on failure`() = runTest {
        val mockRepo = FakeAgentsRepositoryForTest()
        mockRepo.setError("Network error")
        val result = mockRepo.getAgents().first()
        assertTrue(result is DataState.Error)
        assertNotNull(result.exception)
    }
    @Test
    fun `getAgentsFromLocalDatabase returns cached data`() = runTest {
        val mockRepo = FakeAgentsRepositoryForTest()
        val cachedAgents = listOf(createMockAgent("1", "Cached Agent"))
        mockRepo.setCachedAgents(cachedAgents)
        val result = mockRepo.getAgentsFromLocalDatabase()
        assertEquals(1, result.size)
        assertEquals("Cached Agent", result[0].displayName)
    }
    @Test
    fun `getAgentsFromLocalDatabase returns empty list when no cache`() = runTest {
        val mockRepo = FakeAgentsRepositoryForTest()
        val result = mockRepo.getAgentsFromLocalDatabase()
        assertTrue(result.isEmpty())
    }
    @Test
    fun `repository handles null values gracefully`() = runTest {
        val mockRepo = FakeAgentsRepositoryForTest()
        val agentsWithNulls = listOf(
            AgentsResponseModel(
                uuid = "1",
                displayName = null,
                fullPortrait = null,
                role = null,
                fullPortraitV2 = null
            )
        )
        mockRepo.setAgents(agentsWithNulls)
        val result = mockRepo.getAgents().first()
        assertTrue(result is DataState.Success)
        val agents = result.data.data
        assertNotNull(agents)
        assertNotNull(agents[0])
    }

    @Test
    fun `insertAgentsToLocalDatabase stores agents`() = runTest {
        val mockRepo = FakeAgentsRepositoryForTest()
        val agents = listOf(
            createMockAgent("1", "Jett"),
            createMockAgent("2", "Phoenix")
        )
        mockRepo.insertAgentsToLocalDatabase(agents)
        val cached = mockRepo.getAgentsFromLocalDatabase()
        assertEquals(2, cached.size)
        assertEquals("Jett", cached[0].displayName)
    }

    private fun createMockAgent(uuid: String, displayName: String) = AgentsResponseModel(
        uuid = uuid,
        displayName = displayName,
        fullPortrait = "portrait.png",
        role = AgentsRoleModel(displayName = "Duelist"),
        fullPortraitV2 = "portraitv2.png"
    )
}

class FakeAgentsRepositoryForTest : AgentsRepo {
    private var agents: List<AgentsResponseModel> = emptyList()
    private var cachedAgents: List<AgentsResponseModel> = emptyList()
    private var agentDetails: AgentDetailsData? = null
    private var error: String? = null

    fun setAgents(agentsList: List<AgentsResponseModel>) {
        agents = agentsList
        error = null
    }

    fun setCachedAgents(agentsList: List<AgentsResponseModel>) {
        cachedAgents = agentsList
    }

    fun setAgentDetailsData(details: AgentDetailsData) {
        agentDetails = details
        error = null
    }

    fun setError(errorMessage: String) {
        error = errorMessage
        agents = emptyList()
        agentDetails = null
    }

    override suspend fun getAgents(): Flow<DataState<BaseResponse<List<AgentsResponseModel>>>> = flow {
        error?.let {
            emit(DataState.Error(Exception(it)))
        } ?: emit(DataState.Success(BaseResponse(data = agents)))
    }
    override suspend fun getAgentDetails(id: String): Flow<DataState<BaseResponse<AgentDetailsData>>> = flow {
        error?.let {
            emit(DataState.Error(Exception(it)))
        } ?: agentDetails?.let {
            emit(DataState.Success(BaseResponse(data = it)))
        } ?: emit(DataState.Success(BaseResponse(data = AgentDetailsData())))
    }

    override suspend fun getAgentsFromLocalDatabase(): List<AgentsResponseModel> {
        return cachedAgents
    }

    override suspend fun insertAgentsToLocalDatabase(agents: List<AgentsResponseModel>) {
        cachedAgents = agents
    }
}
@OptIn(ExperimentalCoroutinesApi::class)
class AgentDetailsRepositoryTest {
    @Test
    fun `getAgentDetails returns correct agent data`() = runTest {
        val mockRepo = FakeAgentsRepositoryForTest()
        val expectedAgent = createMockAgentDetails("jett-id", "Jett")
        mockRepo.setAgentDetailsData(expectedAgent)
        val result = mockRepo.getAgentDetails("jett-id").first()
        assertTrue(result is DataState.Success)
        val agent = result.data.data
        assertNotNull(agent)
        assertEquals("Jett", agent.displayName)
        assertEquals("jett-id", agent.uuid)
    }
    @Test
    fun `getAgentDetails returns error for invalid id`() = runTest {
        val mockRepo = FakeAgentsRepositoryForTest()
        mockRepo.setError("Agent not found")
        val result = mockRepo.getAgentDetails("invalid-id").first()
        assertTrue(result is DataState.Error)
        assertNotNull(result.exception)
    }
    @Test
    fun `getAgentDetails with valid id returns data`() = runTest {
        val mockRepo = FakeAgentsRepositoryForTest()
        val agent = createMockAgentDetails("cached-id", "Cached Agent")
        mockRepo.setAgentDetailsData(agent)
        val result = mockRepo.getAgentDetails("cached-id").first()
        assertTrue(result is DataState.Success)
        val agentData = result.data.data
        assertNotNull(agentData)
        assertEquals("Cached Agent", agentData.displayName)
    }
    private fun createMockAgentDetails(uuid: String, displayName: String) = AgentDetailsData(
        uuid = uuid,
        displayName = displayName,
        description = "Description",
        developerName = "dev-name",
        characterTags = null,
        displayIcon = "icon.png",
        displayIconSmall = "icon-small.png",
        bustPortrait = "bust.png",
        fullPortrait = "portrait.png",
        fullPortraitV2 = "portrait-v2.png",
        killfeedPortrait = "killfeed.png",
        background = "bg.png",
        backgroundGradientColors = emptyList(),
        assetPath = "path",
        isFullPortraitRightFacing = true,
        isPlayableCharacter = true,
        isAvailableForTest = true,
        isBaseContent = true,
        role = null,
        recruitmentData = null,
        abilities = emptyList(),
        voiceLine = null
    )
}
