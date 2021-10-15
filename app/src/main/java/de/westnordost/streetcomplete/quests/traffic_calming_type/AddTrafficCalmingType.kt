package de.westnordost.streetcomplete.quests.barrier_type

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.osm.edits.update_tags.StringMapChangesBuilder
import de.westnordost.streetcomplete.data.osm.osmquests.OsmFilterQuestType
import de.westnordost.streetcomplete.data.user.achievements.QuestTypeAchievement.PEDESTRIAN
import de.westnordost.streetcomplete.data.user.achievements.QuestTypeAchievement.CAR
import de.westnordost.streetcomplete.quests.traffic_calming_type.AddTrafficCalmingTypeForm
import de.westnordost.streetcomplete.quests.traffic_calming_type.TrafficCalmingType

class AddTrafficCalmingType : OsmFilterQuestType<TrafficCalmingType>() {

    override val elementFilter = """
        nodes with traffic_calming=yes
    """
    override val commitMessage = "Add specific traffic calming type on a point"
    override val wikiLink = "Key:traffic_calming"
    override val icon = R.drawable.ic_quest_traffic_calming
    override val isDeleteElementEnabled = true

    override fun getTitle(tags: Map<String, String>) = R.string.quest_traffic_calming_type_title

    override fun createForm() = AddTrafficCalmingTypeForm()

    override fun applyAnswerTo(answer: TrafficCalmingType, changes: StringMapChangesBuilder) {
        changes.modify("traffic_calming", answer.osmValue)
    }

    override val questTypeAchievements = listOf(PEDESTRIAN, CAR)
}
