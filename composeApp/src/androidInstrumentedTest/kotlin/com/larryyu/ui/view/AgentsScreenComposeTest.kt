package com.larryyu.ui.view
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.larryyu.domain.model.AgentsModel
import com.larryyu.domain.model.Role
import com.larryyu.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
@RunWith(AndroidJUnit4::class)
class AgentsScreenComposeTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    @Test
    fun agentsScreen_showsLoadingIndicator_whenLoading() {
        val isLoading = true
        val agents = emptyList<AgentsModel>()
        composeTestRule.setContent {
            AppTheme {
                AgentsGridContent(
                    agents = agents,
                    isLoading = isLoading,
                    onAgentClick = {}
                )
            }
        }
        composeTestRule
            .onNodeWithTag("agents_loading_indicator")
            .assertExists()
            .assertIsDisplayed()
    }
    @Test
    fun agentsScreen_showsAgentsList_whenDataLoaded() {
        val agents = listOf(
            createMockAgent("1", "Jett"),
            createMockAgent("2", "Phoenix"),
            createMockAgent("3", "Sage")
        )
        val isLoading = false
        composeTestRule.setContent {
            AppTheme {
                AgentsGridContent(
                    agents = agents,
                    isLoading = isLoading,
                    onAgentClick = {}
                )
            }
        }
        composeTestRule
            .onNodeWithText("Jett")
            .assertExists()
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Phoenix")
            .assertExists()
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Sage")
            .assertExists()
            .assertIsDisplayed()
    }
    @Test
    fun agentsScreen_agentCard_isClickable() {
        var clickedAgentId: String? = null
        val agent = createMockAgent("jett-id", "Jett")
        composeTestRule.setContent {
            AppTheme {
                AgentsGridContent(
                    agents = listOf(agent),
                    isLoading = false,
                    onAgentClick = { clickedAgentId = it.uuid }
                )
            }
        }
        composeTestRule
            .onNodeWithText("Jett")
            .performClick()
        assert(clickedAgentId == "jett-id") { "Agent click should be registered" }
    }
    @Test
    fun agentsScreen_displaysEmptyState_whenNoAgents() {
        val agents = emptyList<AgentsModel>()
        val isLoading = false
        composeTestRule.setContent {
            AppTheme {
                AgentsGridContent(
                    agents = agents,
                    isLoading = isLoading,
                    onAgentClick = {}
                )
            }
        }
        composeTestRule
            .onNodeWithTag("agents_grid")
            .assertExists()
    }
    @Test
    fun agentsScreen_displaysMultipleAgents_inGrid() {
        val agents = (1..10).map { i ->
            createMockAgent("agent-$i", "Agent $i")
        }
        composeTestRule.setContent {
            AppTheme {
                AgentsGridContent(
                    agents = agents,
                    isLoading = false,
                    onAgentClick = {}
                )
            }
        }
        agents.forEach { agent ->
            composeTestRule
                .onNodeWithText(agent.displayName ?: "")
                .assertExists()
        }
    }
    @Test
    fun agentsScreen_agentCard_displaysAgentInfo() {
        val agent = createMockAgent(
            uuid = "jett-id",
            displayName = "Jett",
            roleName = "Duelist"
        )
        composeTestRule.setContent {
            AppTheme {
                AgentsGridContent(
                    agents = listOf(agent),
                    isLoading = false,
                    onAgentClick = {}
                )
            }
        }
        composeTestRule
            .onNodeWithText("Jett")
            .assertExists()
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Duelist", substring = true)
            .assertExists()
    }
    @Test
    fun agentsScreen_scrolling_worksCorrectly() {
        val agents = (1..20).map { i ->
            createMockAgent("agent-$i", "Agent $i")
        }
        composeTestRule.setContent {
            AppTheme {
                AgentsGridContent(
                    agents = agents,
                    isLoading = false,
                    onAgentClick = {}
                )
            }
        }
        composeTestRule
            .onNodeWithTag("agents_grid")
            .performScrollToNode(hasText("Agent 20"))
        composeTestRule
            .onNodeWithText("Agent 20")
            .assertIsDisplayed()
    }
    @Test
    fun agentsScreen_multipleClicks_handleCorrectly() {
        val clickedAgents = mutableListOf<String>()
        val agents = listOf(
            createMockAgent("1", "Agent 1"),
            createMockAgent("2", "Agent 2"),
            createMockAgent("3", "Agent 3")
        )
        composeTestRule.setContent {
            AppTheme {
                AgentsGridContent(
                    agents = agents,
                    isLoading = false,
                    onAgentClick = { agent -> clickedAgents.add(agent.uuid) }
                )
            }
        }
        composeTestRule.onNodeWithText("Agent 1").performClick()
        composeTestRule.onNodeWithText("Agent 2").performClick()
        composeTestRule.onNodeWithText("Agent 1").performClick()
        assert(clickedAgents.size == 3) { "Should register all clicks" }
        assert(clickedAgents[0] == "1")
        assert(clickedAgents[1] == "2")
        assert(clickedAgents[2] == "1")
    }
    @Test
    fun agentsScreen_roleIcon_isDisplayed() {
        val agent = createMockAgent("jett-id", "Jett", "Duelist")
        composeTestRule.setContent {
            AppTheme {
                AgentsGridContent(
                    agents = listOf(agent),
                    isLoading = false,
                    onAgentClick = {}
                )
            }
        }
        composeTestRule
            .onNodeWithContentDescription("Agent role icon", substring = true)
            .assertExists()
    }
    private fun createMockAgent(
        uuid: String,
        displayName: String,
        roleName: String = "Duelist"
    ) = AgentsModel(
        uuid = uuid,
        displayName = displayName,
        fullPortrait = "https://example.com/portrait.png",
        role = Role(displayName = roleName),
        fullPortraitV2 = "https://example.com/portrait-v2.png"
    )
}
@Composable
private fun AgentsGridContent(
    agents: List<AgentsModel>,
    isLoading: Boolean,
    onAgentClick: (AgentsModel) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.testTag("agents_loading_indicator")
                )
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("agents_grid"),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = agents,
                        key = { it.uuid }
                    ) { agent ->
                        AgentCard(
                            agent = agent,
                            onClick = { onAgentClick(agent) }
                        )
                    }
                }
            }
        }
    }
}
@Composable
private fun AgentCard(
    agent: AgentsModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.7f)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = agent.displayName ?: "",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = agent.role?.displayName ?: "",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(
                modifier = Modifier
                    .size(24.dp)
                    .semantics { contentDescription = "Agent role icon" }
            )
        }
    }
}
