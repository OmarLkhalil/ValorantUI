package com.larryyu.integration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.larryyu.presentation.model.AgentUiModel
import com.larryyu.presentation.model.AgentDetailsUiModel
import com.larryyu.presentation.model.AgentRoleDetailsUi
import com.larryyu.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
@RunWith(AndroidJUnit4::class)
class AgentsIntegrationTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    @Test
    fun userFlow_selectAgent_navigatesToDetails() {
        val agents = listOf(
            createMockAgent("jett-id", "Jett"),
            createMockAgent("phoenix-id", "Phoenix")
        )
        var selectedAgentId: String? = null
        composeTestRule.setContent {
            AppTheme {
                var showDetails by remember { mutableStateOf(false) }
                if (!showDetails) {
                    AgentsListScreen(
                        agents = agents,
                        onAgentClick = { agent ->
                            selectedAgentId = agent.agentId
                            showDetails = true
                        }
                    )
                } else {
                    AgentDetailsScreen(
                        agentId = selectedAgentId ?: "",
                        onBackClick = { showDetails = false }
                    )
                }
            }
        }
        composeTestRule
            .onNodeWithText("Jett")
            .performClick()
        composeTestRule.waitForIdle()
        assert(selectedAgentId == "jett-id") { "Should navigate to Jett details" }
    }
    @Test
    fun userFlow_viewDetails_navigateBack() {
        val agent = createMockAgent("jett-id", "Jett")
        var isOnDetailScreen by mutableStateOf(true)
        composeTestRule.setContent {
            AppTheme {
                if (isOnDetailScreen) {
                    AgentDetailsScreen(
                        agentId = agent.agentId,
                        onBackClick = { isOnDetailScreen = false }
                    )
                } else {
                    AgentsListScreen(
                        agents = listOf(agent),
                        onAgentClick = {}
                    )
                }
            }
        }
        composeTestRule
            .onNodeWithContentDescription("Back")
            .performClick()
        composeTestRule.waitForIdle()
        assert(!isOnDetailScreen) { "Should navigate back to list" }
    }
    @Test
    fun userFlow_browseMultipleAgents() {
        val agents = listOf(
            createMockAgent("jett-id", "Jett"),
            createMockAgent("phoenix-id", "Phoenix"),
            createMockAgent("sage-id", "Sage")
        )
        val viewedAgents = mutableListOf<String>()
        composeTestRule.setContent {
            AppTheme {
                var selectedAgent by remember { mutableStateOf<String?>(null) }
                if (selectedAgent == null) {
                    AgentsListScreen(
                        agents = agents,
                        onAgentClick = { agent ->
                            viewedAgents.add(agent.agentName)
                            selectedAgent = agent.agentId
                        }
                    )
                } else {
                    AgentDetailsScreen(
                        agentId = selectedAgent ?: "",
                        onBackClick = { selectedAgent = null }
                    )
                }
            }
        }
        composeTestRule.onNodeWithText("Jett").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Phoenix").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        composeTestRule.waitForIdle()
        assert(viewedAgents.size == 2) { "Should view 2 agents" }
        assert(viewedAgents.contains("Jett"))
        assert(viewedAgents.contains("Phoenix"))
    }
    @Test
    fun userFlow_searchAgent_viewDetails() {
        val agents = listOf(
            createMockAgent("jett-id", "Jett"),
            createMockAgent("phoenix-id", "Phoenix"),
            createMockAgent("sage-id", "Sage")
        )
        composeTestRule.setContent {
            AppTheme {
                var searchQuery by remember { mutableStateOf("") }
                var selectedAgent by remember { mutableStateOf<String?>(null) }
                if (selectedAgent == null) {
                    AgentsListWithSearch(
                        agents = agents,
                        searchQuery = searchQuery,
                        onSearchChange = { searchQuery = it },
                        onAgentClick = { agent -> selectedAgent = agent.agentId }
                    )
                } else {
                    AgentDetailsScreen(
                        agentId = selectedAgent ?: "",
                        onBackClick = { selectedAgent = null }
                    )
                }
            }
        }
        composeTestRule
            .onNodeWithTag("search_field")
            .performTextInput("Jett")
        composeTestRule.waitForIdle()
        composeTestRule
            .onNodeWithText("Jett")
            .performClick()
        composeTestRule.waitForIdle()
    }
    @Test
    fun userFlow_emptyState_handledGracefully() {
        val agents = emptyList<AgentUiModel>()
        composeTestRule.setContent {
            AppTheme {
                AgentsListScreen(
                    agents = agents,
                    onAgentClick = {}
                )
            }
        }
        composeTestRule
            .onNodeWithTag("agents_grid")
            .assertExists()
    }
    @Test
    fun userFlow_rapidNavigation_handlesCorrectly() {
        val agent = createMockAgent("jett-id", "Jett")
        composeTestRule.setContent {
            AppTheme {
                var showDetails by remember { mutableStateOf(false) }
                if (!showDetails) {
                    AgentsListScreen(
                        agents = listOf(agent),
                        onAgentClick = { showDetails = true }
                    )
                } else {
                    AgentDetailsScreen(
                        agentId = agent.agentId,
                        onBackClick = { showDetails = false }
                    )
                }
            }
        }
        repeat(3) {
            composeTestRule.onNodeWithText("Jett").performClick()
            composeTestRule.waitForIdle()
            composeTestRule.onNodeWithContentDescription("Back").performClick()
            composeTestRule.waitForIdle()
        }
        composeTestRule
            .onNodeWithText("Jett")
            .assertIsDisplayed()
    }
    private fun createMockAgent(uuid: String, displayName: String) = AgentUiModel(
        agentId = uuid,
        agentName = displayName,
        agentImageUrl = "https://example.com/portrait.png",
        agentRole = "Duelist"
    )
}
@Composable
private fun AgentsListScreen(
    agents: List<AgentUiModel>,
    onAgentClick: (AgentUiModel) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .testTag("agents_grid"),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(
            items = agents,
            key = { it.agentId }
        ) { agent ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onAgentClick(agent) }
            ) {
                Text(
                    text = agent.agentName,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
@Composable
private fun AgentsListWithSearch(
    agents: List<AgentUiModel>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onAgentClick: (AgentUiModel) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("search_field"),
            placeholder = { Text("Search agents...") }
        )
        val filteredAgents = agents.filter {
            it.agentName.contains(searchQuery, ignoreCase = true)
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .testTag("agents_grid")
        ) {
            items(
                items = filteredAgents,
                key = { it.agentId }
            ) { agent ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { onAgentClick(agent) }
                ) {
                    Text(
                        text = agent.agentName,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
@Composable
private fun AgentDetailsScreen(
    agentId: String,
    onBackClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.testTag("back_button")
        ) {
            Text("←", style = MaterialTheme.typography.headlineMedium)
        }
        Text(
            text = "Agent Details: $agentId",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}
