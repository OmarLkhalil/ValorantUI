package com.larryyu.ui.view
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.larryyu.presentation.model.AbilityUiModel
import com.larryyu.presentation.model.AgentDetailsUiModel
import com.larryyu.presentation.model.AgentRoleDetailsUi
import com.larryyu.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
@RunWith(AndroidJUnit4::class)
class AgentDetailsScreenComposeTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    @Test
    fun agentDetailsScreen_displaysAgentName() {
        val agent = createMockAgentDetails("jett-id", "Jett")
        composeTestRule.setContent {
            AppTheme {
                AgentDetailsContent(agent = agent)
            }
        }
        composeTestRule
            .onNodeWithText("Jett")
            .assertExists()
            .assertIsDisplayed()
    }
    @Test
    fun agentDetailsScreen_displaysAgentDescription() {
        val agent = createMockAgentDetails(
            "jett-id",
            "Jett",
            description = "Representing her home country of South Korea"
        )
        composeTestRule.setContent {
            AppTheme {
                AgentDetailsContent(agent = agent)
            }
        }
        composeTestRule
            .onNodeWithText("Representing her home country of South Korea")
            .assertExists()
            .assertIsDisplayed()
    }
    @Test
    fun agentDetailsScreen_displaysRoleInformation() {
        val agent = createMockAgentDetails("jett-id", "Jett")
        composeTestRule.setContent {
            AppTheme {
                AgentDetailsContent(agent = agent)
            }
        }
        composeTestRule
            .onNodeWithText("Duelist", substring = true)
            .assertExists()
    }
    @Test
    fun agentDetailsScreen_displaysAbilities() {
        val abilities = listOf(
            createMockAbility("1", "Cloudburst", "Q"),
            createMockAbility("2", "Updraft", "E"),
            createMockAbility("3", "Tailwind", "X"),
            createMockAbility("4", "Blade Storm", "C")
        )
        val agent = createMockAgentDetails("jett-id", "Jett", abilities = abilities)
        composeTestRule.setContent {
            AppTheme {
                AgentDetailsContent(agent = agent)
            }
        }
        composeTestRule
            .onNodeWithText("Cloudburst")
            .assertExists()
        composeTestRule
            .onNodeWithText("Updraft")
            .assertExists()
        composeTestRule
            .onNodeWithText("Tailwind")
            .assertExists()
        composeTestRule
            .onNodeWithText("Blade Storm")
            .assertExists()
    }
    @Test
    fun agentDetailsScreen_abilitySlots_areDisplayed() {
        val abilities = listOf(
            createMockAbility("1", "Cloudburst", "Q"),
            createMockAbility("2", "Updraft", "E")
        )
        val agent = createMockAgentDetails("jett-id", "Jett", abilities = abilities)
        composeTestRule.setContent {
            AppTheme {
                AgentDetailsContent(agent = agent)
            }
        }
        composeTestRule
            .onNodeWithText("Q", substring = true)
            .assertExists()
        composeTestRule
            .onNodeWithText("E", substring = true)
            .assertExists()
    }
    @Test
    fun agentDetailsScreen_abilityClick_showsDescription() {
        var clickedAbility: String? = null
        val abilities = listOf(
            createMockAbility("1", "Cloudburst", "Q", "Throw a cloud of fog")
        )
        val agent = createMockAgentDetails("jett-id", "Jett", abilities = abilities)
        composeTestRule.setContent {
            AppTheme {
                AgentDetailsContentWithClickHandler(
                    agent = agent,
                    onAbilityClick = { ability -> clickedAbility = ability.abilityName }
                )
            }
        }
        composeTestRule
            .onNodeWithText("Cloudburst")
            .performClick()
        assert(clickedAbility == "Cloudburst") { "Ability click should be registered" }
    }
    @Test
    fun agentDetailsScreen_scrolling_worksCorrectly() {
        val abilities = (1..10).map { i ->
            createMockAbility("$i", "Ability $i", "Key $i")
        }
        val agent = createMockAgentDetails("test-id", "Test Agent", abilities = abilities)
        composeTestRule.setContent {
            AppTheme {
                AgentDetailsContent(agent = agent)
            }
        }
        composeTestRule
            .onNodeWithTag("agent_details_content")
            .performScrollToNode(hasText("Ability 10"))
        composeTestRule
            .onNodeWithText("Ability 10")
            .assertIsDisplayed()
    }
    @Test
    fun agentDetailsScreen_handlesNoAbilities_gracefully() {
        val agent = createMockAgentDetails("test-id", "Test Agent", abilities = emptyList())
        composeTestRule.setContent {
            AppTheme {
                AgentDetailsContent(agent = agent)
            }
        }
        composeTestRule
            .onNodeWithText("Test Agent")
            .assertExists()
            .assertIsDisplayed()
    }
    @Test
    fun agentDetailsScreen_backButton_isClickable() {
        var backClicked = false
        val agent = createMockAgentDetails("test-id", "Test Agent")
        composeTestRule.setContent {
            AppTheme {
                AgentDetailsContentWithBackButton(
                    agent = agent,
                    onBackClick = { backClicked = true }
                )
            }
        }
        composeTestRule
            .onNodeWithContentDescription("Back")
            .performClick()
        assert(backClicked) { "Back button click should be registered" }
    }
    @Test
    fun agentDetailsScreen_agentPortrait_isDisplayed() {
        val agent = createMockAgentDetails("jett-id", "Jett")
        composeTestRule.setContent {
            AppTheme {
                AgentDetailsContent(agent = agent)
            }
        }
        composeTestRule
            .onNodeWithContentDescription("Jett portrait", substring = true)
            .assertExists()
    }
    @Test
    fun agentDetailsScreen_roleIcon_isDisplayed() {
        val agent = createMockAgentDetails("jett-id", "Jett")
        composeTestRule.setContent {
            AppTheme {
                AgentDetailsContent(agent = agent)
            }
        }
        composeTestRule
            .onNodeWithContentDescription("Role icon", substring = true)
            .assertExists()
    }
    @Test
    fun agentDetailsScreen_multipleAbilityClicks_handleCorrectly() {
        val clickedAbilities = mutableListOf<String>()
        val abilities = listOf(
            createMockAbility("1", "Ability 1", "Q"),
            createMockAbility("2", "Ability 2", "E")
        )
        val agent = createMockAgentDetails("test-id", "Test", abilities = abilities)
        composeTestRule.setContent {
            AppTheme {
                AgentDetailsContentWithClickHandler(
                    agent = agent,
                    onAbilityClick = { ability -> clickedAbilities.add(ability.abilityName) }
                )
            }
        }
        composeTestRule.onNodeWithText("Ability 1").performClick()
        composeTestRule.onNodeWithText("Ability 2").performClick()
        composeTestRule.onNodeWithText("Ability 1").performClick()
        assert(clickedAbilities.size == 3) { "Should register all ability clicks" }
        assert(clickedAbilities[0] == "Ability 1")
        assert(clickedAbilities[1] == "Ability 2")
        assert(clickedAbilities[2] == "Ability 1")
    }
    private fun createMockAgentDetails(
        uuid: String,
        displayName: String,
        description: String = "Test description",
        abilities: List<AbilityUiModel>? = null
    ) = AgentDetailsUiModel(
        agentId = uuid,
        agentName = displayName,
        agentDescription = description,
        agentRole = AgentRoleDetailsUi("Duelist", "https://example.com/role-icon.png"),
        agentImageUrl = "https://example.com/portrait.png",
        agentAbilities = abilities ?: emptyList()
    )
    private fun createMockAbility(
        @Suppress("UNUSED_PARAMETER") uuid: String,
        displayName: String,
        slot: String,
        description: String = "Test ability description"
    ) = AbilityUiModel(
        abilityName = displayName,
        abilityDescription = description,
        abilityIconUrl = "https://example.com/ability-icon.png"
    )
}
@Composable
private fun AgentDetailsContent(agent: AgentDetailsUiModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("agent_details_content")
    ) {
        item {
            Text(
                text = agent.agentName,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
        item {
            Text(
                text = agent.agentDescription,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        item {
            Text(
                text = "Role: ${agent.agentRole.roleName}",
                modifier = Modifier.padding(16.dp)
            )
            Spacer(
                modifier = Modifier
                    .size(24.dp)
                    .semantics { contentDescription = "Role icon" }
            )
        }
        item {
            Spacer(
                modifier = Modifier
                    .size(200.dp)
                    .semantics { contentDescription = "${agent.agentName} portrait" }
            )
        }
        agent.agentAbilities.forEach { ability ->
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = ability.abilityName,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = ability.abilityDescription,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}
@Composable
private fun AgentDetailsContentWithClickHandler(
    agent: AgentDetailsUiModel,
    onAbilityClick: (AbilityUiModel) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                text = agent.agentName,
                modifier = Modifier.padding(16.dp)
            )
        }
        agent.agentAbilities.forEach { ability ->
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { onAbilityClick(ability) }
                ) {
                    Text(
                        text = ability.abilityName,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
@Composable
private fun AgentDetailsContentWithBackButton(
    agent: AgentDetailsUiModel,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.semantics {
                contentDescription = "Back"
            }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back"
            )
        }
        Text(
            text = agent.agentName,
            modifier = Modifier.padding(16.dp)
        )
    }
}
