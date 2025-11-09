package com.larryyu.valorantui

import com.larryyu.valorantui.domain.entitiy.BaseResponse
import com.larryyu.valorantui.domain.model.AgentsModel
import com.larryyu.valorantui.domain.model.Role
import com.larryyu.valorantui.domain.usecase.AgentsUseCase
import com.larryyu.valorantui.domain.utils.DataState
import com.larryyu.valorantui.ui.contract.AgentsIntent
import com.larryyu.valorantui.ui.viewmodel.AgentsViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class AgentsViewModelTest {

    private lateinit var viewModel: AgentsViewModel
    private lateinit var agentsUseCase: AgentsUseCase

    private val testDispatch = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatch)
        agentsUseCase = mock()
        viewModel = AgentsViewModel(agentsUseCase)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `fake agents list has 4 agents`() {
        val agents = FakeAgentsData.fakeAgentsList()
        assertEquals(4, agents.size)
        assertEquals("Jett", agents[0].displayName)
        assertEquals("Duelist", agents[0].role?.displayName)
    }

    @Test
    fun `when fetchAgents success then state contains agents`() = runTest {
        val agentsList = FakeAgentsData.fakeAgentsList()
        whenever(agentsUseCase.invoke()).thenReturn(
            flowOf(DataState.Success(BaseResponse(data = agentsList)))
        )

        viewModel.dispatch(AgentsIntent.FetchAgents)

        advanceUntilIdle()
        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(agentsList, state.agents)
        assertEquals("", state.error)
    }

    @Test
    fun `when fetchAgents error then state has error`() = runTest {
        val errorMessage = "Network Error"
        whenever(agentsUseCase.invoke()).thenReturn(
            flowOf(DataState.Error(Exception(errorMessage)))
        )

        viewModel.dispatch(AgentsIntent.FetchAgents)

        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(errorMessage, state.error)
        assertTrue(state.agents.isEmpty())
    }

    @Test
    fun `when fetchAgents loading then state is loading`() = runTest {
        whenever(agentsUseCase.invoke()).thenReturn(
            flowOf(DataState.Loading)
        )

        viewModel.dispatch(AgentsIntent.FetchAgents)

        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state.isLoading)
    }

    @Test
    fun `when fetchAgents idle then state is not loading`() = runTest {
        whenever(agentsUseCase.invoke()).thenReturn(
            flowOf(DataState.Idle)
        )

        viewModel.dispatch(AgentsIntent.FetchAgents)

        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
    }
}


object FakeAgentsData {

    fun fakeAgent(
        id: String = "1",
        name: String = "Jett",
        portrait: String = "https://example.com/jett.png",
        role: String = "Duelist"
    ): AgentsModel {
        return AgentsModel(
            displayName = name,
            uuid = id,
            fullPortrait = portrait,
            fullPortraitV2 = portrait,
            role = Role(displayName = role)
        )
    }

    fun fakeAgentsList(): List<AgentsModel> {
        return listOf(
            fakeAgent(id = "1", name = "Jett", role = "Duelist"),
            fakeAgent(id = "2", name = "Sova", role = "Initiator"),
            fakeAgent(id = "3", name = "Sage", role = "Sentinel"),
            fakeAgent(id = "4", name = "Omen", role = "Controller")
        )
    }
}
