package org.optaplanner.examples.conferencescheduling.solver;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.junit.Test;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.examples.conferencescheduling.app.ConferenceSchedulingApp;
import org.optaplanner.examples.conferencescheduling.domain.ConferenceParametrization;
import org.optaplanner.examples.conferencescheduling.domain.ConferenceSolution;
import org.optaplanner.examples.conferencescheduling.domain.Room;
import org.optaplanner.examples.conferencescheduling.domain.Speaker;
import org.optaplanner.examples.conferencescheduling.domain.Talk;
import org.optaplanner.examples.conferencescheduling.domain.Timeslot;
import org.optaplanner.test.impl.score.buildin.hardsoft.HardSoftScoreVerifier;

import static org.optaplanner.examples.conferencescheduling.domain.ConferenceParametrization.*;

public class ConferenceSchedulingScoreSoftConstraintTest {

    private HardSoftScoreVerifier<ConferenceSolution> scoreVerifier = new HardSoftScoreVerifier<>(
            SolverFactory.createFromXmlResource(ConferenceSchedulingApp.SOLVER_CONFIG));

    @Test
    public void themeConflict() {
        ConferenceParametrization parametrization = new ConferenceParametrization(1L);
        String talkType = "type1";
        String theme1 = "theme1";
        String theme2 = "theme2";
        String theme3 = "theme3";
        String theme4 = "theme4";
        Talk talk1 = new Talk(1L)
                .withTalkType(talkType)
                .withSpeakerList(Collections.emptyList())
                .withRequiredTimeslotTagSet(Collections.emptySet())
                .withPreferredTimeslotTagSet(Collections.emptySet())
                .withProhibitedTimeslotTagSet(Collections.emptySet())
                .withUndesiredTimeslotTagSet(Collections.emptySet())
                .withSectorTagSet(Collections.emptySet());
        Talk talk2 = new Talk(2L)
                .withTalkType(talkType)
                .withSpeakerList(Collections.emptyList())
                .withRequiredTimeslotTagSet(Collections.emptySet())
                .withPreferredTimeslotTagSet(Collections.emptySet())
                .withProhibitedTimeslotTagSet(Collections.emptySet())
                .withUndesiredTimeslotTagSet(Collections.emptySet())
                .withSectorTagSet(Collections.emptySet());
        LocalDateTime start1 = LocalDateTime.of(2018, 1, 1, 9, 0);
        LocalDateTime end1 = LocalDateTime.of(2018, 1, 1, 10, 0);
        LocalDateTime start2 = LocalDateTime.of(2018, 1, 1, 9, 30);
        LocalDateTime end2 = LocalDateTime.of(2018, 1, 1, 10, 30);
        LocalDateTime start3 = LocalDateTime.of(2018, 1, 1, 10, 0);
        LocalDateTime end3 = LocalDateTime.of(2018, 1, 1, 11, 0);
        Timeslot slot1 = new Timeslot(1L)
                .withTalkType(talkType)
                .withStartDateTime(start1)
                .withEndDateTime(end1);
        Timeslot slot2 = new Timeslot(2L)
                .withTalkType(talkType)
                .withStartDateTime(start2)
                .withEndDateTime(end2);
        Timeslot slot3 = new Timeslot(3L)
                .withTalkType(talkType)
                .withStartDateTime(start3)
                .withEndDateTime(end3);
        ConferenceSolution solution = new ConferenceSolution(1L)
                .withParametrization(parametrization)
                .withTalkList(Arrays.asList(talk1, talk2))
                .withTimeslotList(Arrays.asList(slot1, slot2, slot3))
                .withRoomList(Collections.emptyList())
                .withSpeakerList(Collections.emptyList());
        parametrization.setThemeTrackConflict(1);
        scoreVerifier.assertSoftWeight(THEME_TRACK_CONFLICT, 0, solution);
        // talks with overlapping time slots without theme track conflict
        talk1.withTimeslot(slot1).withThemeTrackTagSet(new HashSet<>(Arrays.asList(theme1, theme2)));
        talk2.withTimeslot(slot2).withThemeTrackTagSet(new HashSet<>(Arrays.asList(theme3, theme4)));
        scoreVerifier.assertSoftWeight(THEME_TRACK_CONFLICT, 0, solution);
        // talks with overlapping time slots with 1 theme track conflict
        talk2.withThemeTrackTagSet(new HashSet<>(Arrays.asList(theme1, theme3, theme4)));
        scoreVerifier.assertSoftWeight(THEME_TRACK_CONFLICT, -1, solution);
        // talks with overlapping time slots with 2 theme track conflicts
        talk1.withTimeslot(slot1).withThemeTrackTagSet(new HashSet<>(Arrays.asList(theme1, theme2, theme3)));
        scoreVerifier.assertSoftWeight(THEME_TRACK_CONFLICT, -2, solution);
        // talks with overlapping time slots with 2 theme track conflicts and theme conflict weight 2
        parametrization.setThemeTrackConflict(2);
        scoreVerifier.assertSoftWeight(THEME_TRACK_CONFLICT, -4, solution);
        // talks with non overlapping time slots and theme track conflicts
        talk2.setTimeslot(slot3);
        scoreVerifier.assertSoftWeight(THEME_TRACK_CONFLICT, 0, solution);
    }

    @Test
    public void sectorConflict() {
        ConferenceParametrization parametrization = new ConferenceParametrization(1L);
        String talkType = "type1";
        String sector1 = "sector1";
        String sector2 = "sector2";
        String sector3 = "sector3";
        String sector4 = "sector4";
        Talk talk1 = new Talk(1L)
                .withTalkType(talkType)
                .withSpeakerList(Collections.emptyList())
                .withRequiredTimeslotTagSet(Collections.emptySet())
                .withPreferredTimeslotTagSet(Collections.emptySet())
                .withProhibitedTimeslotTagSet(Collections.emptySet())
                .withUndesiredTimeslotTagSet(Collections.emptySet())
                .withThemeTrackTagSet(Collections.emptySet())
                .withSectorTagSet(Collections.emptySet());
        Talk talk2 = new Talk(2L)
                .withTalkType(talkType)
                .withSpeakerList(Collections.emptyList())
                .withRequiredTimeslotTagSet(Collections.emptySet())
                .withPreferredTimeslotTagSet(Collections.emptySet())
                .withProhibitedTimeslotTagSet(Collections.emptySet())
                .withUndesiredTimeslotTagSet(Collections.emptySet())
                .withThemeTrackTagSet(Collections.emptySet())
                .withSectorTagSet(Collections.emptySet());
        LocalDateTime start1 = LocalDateTime.of(2018, 1, 1, 9, 0);
        LocalDateTime end1 = LocalDateTime.of(2018, 1, 1, 10, 0);
        LocalDateTime start2 = LocalDateTime.of(2018, 1, 1, 9, 30);
        LocalDateTime end2 = LocalDateTime.of(2018, 1, 1, 10, 30);
        LocalDateTime start3 = LocalDateTime.of(2018, 1, 1, 10, 0);
        LocalDateTime end3 = LocalDateTime.of(2018, 1, 1, 11, 0);
        Timeslot slot1 = new Timeslot(1L)
                .withTalkType(talkType)
                .withStartDateTime(start1)
                .withEndDateTime(end1);
        Timeslot slot2 = new Timeslot(2L)
                .withTalkType(talkType)
                .withStartDateTime(start2)
                .withEndDateTime(end2);
        Timeslot slot3 = new Timeslot(3L)
                .withTalkType(talkType)
                .withStartDateTime(start3)
                .withEndDateTime(end3);
        ConferenceSolution solution = new ConferenceSolution(1L)
                .withParametrization(parametrization)
                .withTalkList(Arrays.asList(talk1, talk2))
                .withTimeslotList(Arrays.asList(slot1, slot2, slot3))
                .withRoomList(Collections.emptyList())
                .withSpeakerList(Collections.emptyList());
        parametrization.setSectorConflict(1);
        scoreVerifier.assertSoftWeight(SECTOR_CONFLICT, 0, solution);
        // talks with overlapping time slots without sector conflict
        parametrization.setSectorConflict(1);
        talk1.withTimeslot(slot1).withSectorTagSet(new HashSet<>(Arrays.asList(sector1, sector2)));
        talk2.withTimeslot(slot2).withSectorTagSet(new HashSet<>(Arrays.asList(sector3, sector4)));
        scoreVerifier.assertSoftWeight(SECTOR_CONFLICT, 0, solution);
        // talks with overlapping time slots with 1 sector conflict
        talk2.withSectorTagSet(new HashSet<>(Arrays.asList(sector1, sector3, sector4)));
        scoreVerifier.assertSoftWeight(SECTOR_CONFLICT, -1, solution);
        // talks with overlapping time slots with 2 sector conflicts
        talk1.withTimeslot(slot1).withSectorTagSet(new HashSet<>(Arrays.asList(sector1, sector2, sector3)));
        scoreVerifier.assertSoftWeight(SECTOR_CONFLICT, -2, solution);
        // talks with overlapping time slots with 2 sector conflicts and sector conflict weight 2
        parametrization.setSectorConflict(2);
        scoreVerifier.assertSoftWeight(SECTOR_CONFLICT, -4, solution);
        // talks with non overlapping time slots and sector conflicts
        talk2.setTimeslot(slot3);
        scoreVerifier.assertSoftWeight(SECTOR_CONFLICT, 0, solution);
    }

    @Test
    public void languageDiversity() {
        ConferenceParametrization parametrization = new ConferenceParametrization(1L);
        String talkType = "talktype";
        String language1 = "language1";
        String language2 = "language2";
        LocalDateTime start1 = LocalDateTime.of(2018, 1, 1, 9, 0);
        LocalDateTime end1 = LocalDateTime.of(2018, 1, 1, 10, 0);
        LocalDateTime start2 = LocalDateTime.of(2018, 1, 1, 9, 30);
        LocalDateTime end2 = LocalDateTime.of(2018, 1, 1, 10, 30);
        Timeslot slot1 = new Timeslot(1L)
                .withTalkType(talkType)
                .withStartDateTime(start1)
                .withEndDateTime(end1);
        Timeslot slot2 = new Timeslot(2L)
                .withTalkType(talkType)
                .withStartDateTime(start2)
                .withEndDateTime(end2);
        Talk talk1 = new Talk(1L)
                .withTalkType(talkType)
                .withSpeakerList(Collections.emptyList())
                .withPreferredTimeslotTagSet(Collections.emptySet())
                .withRequiredTimeslotTagSet(Collections.emptySet())
                .withProhibitedTimeslotTagSet(Collections.emptySet())
                .withUndesiredTimeslotTagSet(Collections.emptySet())
                .withPreferredRoomTagSet(Collections.emptySet())
                .withRequiredRoomTagSet(Collections.emptySet())
                .withThemeTrackTagSet(Collections.emptySet())
                .withSectorTagSet(Collections.emptySet());
        Talk talk2 = new Talk(2L)
                .withTalkType(talkType)
                .withSpeakerList(Collections.emptyList())
                .withPreferredTimeslotTagSet(Collections.emptySet())
                .withRequiredTimeslotTagSet(Collections.emptySet())
                .withProhibitedTimeslotTagSet(Collections.emptySet())
                .withUndesiredTimeslotTagSet(Collections.emptySet())
                .withPreferredRoomTagSet(Collections.emptySet())
                .withRequiredRoomTagSet(Collections.emptySet())
                .withThemeTrackTagSet(Collections.emptySet())
                .withSectorTagSet(Collections.emptySet());
        ConferenceSolution solution = new ConferenceSolution(1L)
                .withParametrization(parametrization)
                .withTalkList(Arrays.asList(talk1, talk2))
                .withTimeslotList(Collections.emptyList())
                .withRoomList(Collections.emptyList())
                .withSpeakerList(Collections.emptyList());
        scoreVerifier.assertSoftWeight(LANGUAGE_DIVERSITY, 0, solution);
        // 2 talks with the same language and the same time slot
        parametrization.setLanguageDiversity(1);
        talk1.withTimeslot(slot1).withLanguage(language1);
        talk2.withTimeslot(slot1).withLanguage(language1);
        scoreVerifier.assertSoftWeight(LANGUAGE_DIVERSITY, 0, solution);
        // 2 talks with the same time slot with different languages
        talk2.withLanguage(language2);
        scoreVerifier.assertSoftWeight(LANGUAGE_DIVERSITY, 1, solution);
        // 2 talks with the same time slot with different languages and language diversity weight = 2
        parametrization.setLanguageDiversity(2);
        scoreVerifier.assertSoftWeight(LANGUAGE_DIVERSITY, 2, solution);
        // 2 talks with different time slots with different languages
        talk2.withTimeslot(slot2);
        scoreVerifier.assertSoftWeight(LANGUAGE_DIVERSITY, 0, solution);
        // 2 talks with different time slot with the same language
        talk2.withLanguage(language1);
        scoreVerifier.assertSoftWeight(LANGUAGE_DIVERSITY, 0, solution);
    }

    @Test
    public void speakerPreferredTimeslot() {
        ConferenceParametrization parametrization = new ConferenceParametrization(1L);
        String tag1 = "tag1";
        String tag2 = "tag2";
        String tag3 = "tag3";
        String talkType = "type1";
        Speaker speaker1 = new Speaker(1L)
                .withUnavailableTimeslotSet(Collections.emptySet())
                .withRequiredTimeslotTagSet(Collections.emptySet())
                .withPreferredTimeslotTagSet(Collections.emptySet())
                .withProhibitedTimeslotTagSet(Collections.emptySet())
                .withUndesiredTimeslotTagSet(Collections.emptySet());
        Speaker speaker2 = new Speaker(2L)
                .withUnavailableTimeslotSet(Collections.emptySet())
                .withRequiredTimeslotTagSet(Collections.emptySet())
                .withPreferredTimeslotTagSet(Collections.emptySet())
                .withProhibitedTimeslotTagSet(Collections.emptySet())
                .withUndesiredTimeslotTagSet(Collections.emptySet());
        Talk talk1 = new Talk(1L)
                .withTalkType(talkType)
                .withSpeakerList(Collections.emptyList())
                .withPreferredTimeslotTagSet(Collections.emptySet())
                .withRequiredTimeslotTagSet(Collections.emptySet())
                .withProhibitedTimeslotTagSet(Collections.emptySet())
                .withUndesiredTimeslotTagSet(Collections.emptySet());
        LocalDateTime start1 = LocalDateTime.of(2018, 1, 1, 9, 0);
        LocalDateTime end1 = LocalDateTime.of(2018, 1, 1, 10, 0);
        Timeslot slot1 = new Timeslot(1L)
                .withTalkType(talkType)
                .withStartDateTime(start1)
                .withEndDateTime(end1);
        ConferenceSolution solution = new ConferenceSolution(1L)
                .withParametrization(parametrization)
                .withTalkList(Arrays.asList(talk1))
                .withTimeslotList(Arrays.asList(slot1))
                .withRoomList(Collections.emptyList())
                .withSpeakerList(Arrays.asList(speaker1, speaker2));
        parametrization.setSpeakerPreferredTimeslotTag(1);
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_TIMESLOT_TAG, 0, solution);
        // talk with 1 speaker, speaker without preferred time slot tag
        slot1.setTagSet(Collections.emptySet());
        talk1.withSpeakerList(Arrays.asList(speaker1)).withTimeslot(slot1);
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_TIMESLOT_TAG, 0, solution);
        slot1.setTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_TIMESLOT_TAG, 0, solution);
        // talk with 1 speaker, speaker with preferred time slot tag, time slot without matching tag
        slot1.setTagSet(Collections.emptySet());
        speaker1.setPreferredTimeslotTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_TIMESLOT_TAG, -1, solution);
        slot1.setTagSet(new HashSet<>(Arrays.asList(tag2, tag3)));
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_TIMESLOT_TAG, -1, solution);
        // talk with 1 speaker, speaker with preferred time slot tag, time slot without matching tag, weight = 2
        parametrization.setSpeakerPreferredTimeslotTag(2);
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_TIMESLOT_TAG, -2, solution);
        // talk with 1 speaker, speaker with preferred time slot tag, time slot with matching tag
        parametrization.setSpeakerPreferredTimeslotTag(1);
        speaker1.setPreferredTimeslotTagSet(new HashSet<>(Arrays.asList(tag1)));
        slot1.setTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_TIMESLOT_TAG, 0, solution);
        slot1.setTagSet(new HashSet<>(Arrays.asList(tag1, tag2)));
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_TIMESLOT_TAG, 0, solution);
        // talk with 1 speaker, speaker with 2 preferred time slot tags
        speaker1.setPreferredTimeslotTagSet(new HashSet<>(Arrays.asList(tag1, tag2)));
        slot1.setTagSet(Collections.emptySet());
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_TIMESLOT_TAG, -2, solution);
        slot1.setTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_TIMESLOT_TAG, -1, solution);
        slot1.setTagSet(new HashSet<>(Arrays.asList(tag1, tag2)));
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_TIMESLOT_TAG, 0, solution);
        //talk with 2 speakers
        talk1.withSpeakerList(Arrays.asList(speaker1, speaker2));
        slot1.setTagSet(Collections.emptySet());
        speaker1.setPreferredTimeslotTagSet(new HashSet<>(Arrays.asList(tag1)));
        speaker2.setPreferredTimeslotTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_TIMESLOT_TAG, -2, solution);
        slot1.setTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_TIMESLOT_TAG, 0, solution);
        slot1.setTagSet(Collections.emptySet());
        speaker1.setPreferredTimeslotTagSet(new HashSet<>(Arrays.asList(tag1, tag2)));
        speaker2.setPreferredTimeslotTagSet(new HashSet<>(Arrays.asList(tag1, tag2)));
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_TIMESLOT_TAG, -4, solution);
        slot1.setTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_TIMESLOT_TAG, -2, solution);
        slot1.setTagSet(Collections.emptySet());
        speaker1.setPreferredTimeslotTagSet(new HashSet<>(Arrays.asList(tag1, tag2)));
        speaker2.setPreferredTimeslotTagSet(new HashSet<>(Arrays.asList(tag1, tag3)));
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_TIMESLOT_TAG, -4, solution);
        slot1.setTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_TIMESLOT_TAG, -2, solution);
        slot1.setTagSet(new HashSet<>(Arrays.asList(tag2)));
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_TIMESLOT_TAG, -3, solution);
        slot1.setTagSet(new HashSet<>(Arrays.asList(tag1, tag2, tag3)));
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_TIMESLOT_TAG, 0, solution);
    }

    @Test
    public void talkPreferredTimeslotTag() {
        ConferenceParametrization parametrization = new ConferenceParametrization(1L);
        String tag1 = "tag1";
        String tag2 = "tag2";
        String talkType = "type1";
        Talk talk1 = new Talk(1L)
                .withTalkType(talkType)
                .withSpeakerList(Collections.emptyList())
                .withPreferredTimeslotTagSet(Collections.emptySet())
                .withRequiredTimeslotTagSet(Collections.emptySet())
                .withProhibitedTimeslotTagSet(Collections.emptySet())
                .withUndesiredTimeslotTagSet(Collections.emptySet());
        LocalDateTime start1 = LocalDateTime.of(2018, 1, 1, 9, 0);
        LocalDateTime end1 = LocalDateTime.of(2018, 1, 1, 10, 0);
        Timeslot slot1 = new Timeslot(1L)
                .withTalkType(talkType)
                .withStartDateTime(start1)
                .withEndDateTime(end1);
        ConferenceSolution solution = new ConferenceSolution(1L)
                .withParametrization(parametrization)
                .withTalkList(Arrays.asList(talk1))
                .withTimeslotList(Arrays.asList(slot1))
                .withRoomList(Collections.emptyList())
                .withSpeakerList(Collections.emptyList());
        parametrization.setTalkPreferredTimeslotTag(1);
        scoreVerifier.assertSoftWeight(TALK_PREFERRED_TIMESLOT_TAG, 0, solution);
        // talk without preferred time slot tags
        slot1.setTagSet(Collections.emptySet());
        talk1.withTimeslot(slot1);
        scoreVerifier.assertSoftWeight(TALK_PREFERRED_TIMESLOT_TAG, 0, solution);
        slot1.setTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(TALK_PREFERRED_TIMESLOT_TAG, 0, solution);
        // talk with preferred time slot tag, time slot without matching tag
        talk1.withPreferredTimeslotTagSet(new HashSet<>(Arrays.asList(tag1)));
        slot1.setTagSet(Collections.emptySet());
        scoreVerifier.assertSoftWeight(TALK_PREFERRED_TIMESLOT_TAG, -1, solution);
        slot1.setTagSet(new HashSet<>(Arrays.asList(tag2)));
        scoreVerifier.assertSoftWeight(TALK_PREFERRED_TIMESLOT_TAG, -1, solution);
        // talk with preferred time slot tag, time slot without matching tag, weight = 2
        parametrization.setTalkPreferredTimeslotTag(2);
        scoreVerifier.assertSoftWeight(TALK_PREFERRED_TIMESLOT_TAG, -2, solution);
        // talk with required time slot tag, time slot with matching tag
        parametrization.setTalkPreferredTimeslotTag(1);
        slot1.setTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(TALK_PREFERRED_TIMESLOT_TAG, 0, solution);
        // talk with 2 preferred time slot tags
        slot1.setTagSet(Collections.emptySet());
        talk1.withPreferredTimeslotTagSet(new HashSet<>(Arrays.asList(tag1, tag2)));
        scoreVerifier.assertSoftWeight(TALK_PREFERRED_TIMESLOT_TAG, -2, solution);
        slot1.setTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(TALK_PREFERRED_TIMESLOT_TAG, -1, solution);
        slot1.setTagSet(new HashSet<>(Arrays.asList(tag1, tag2)));
        scoreVerifier.assertSoftWeight(TALK_PREFERRED_TIMESLOT_TAG, 0, solution);
    }

    @Test
    public void speakerUndesiredTimeslot() {
        ConferenceParametrization parametrization = new ConferenceParametrization(1L);
        String tag1 = "tag1";
        String tag2 = "tag2";
        String tag3 = "tag3";
        String talkType = "type1";
        Speaker speaker1 = new Speaker(1L)
                .withUnavailableTimeslotSet(Collections.emptySet())
                .withRequiredTimeslotTagSet(Collections.emptySet())
                .withPreferredTimeslotTagSet(Collections.emptySet())
                .withProhibitedTimeslotTagSet(Collections.emptySet())
                .withUndesiredTimeslotTagSet(Collections.emptySet());
        Speaker speaker2 = new Speaker(2L)
                .withUnavailableTimeslotSet(Collections.emptySet())
                .withRequiredTimeslotTagSet(Collections.emptySet())
                .withPreferredTimeslotTagSet(Collections.emptySet())
                .withProhibitedTimeslotTagSet(Collections.emptySet())
                .withUndesiredTimeslotTagSet(Collections.emptySet());
        Talk talk1 = new Talk(1L)
                .withTalkType(talkType)
                .withSpeakerList(Collections.emptyList())
                .withPreferredTimeslotTagSet(Collections.emptySet())
                .withRequiredTimeslotTagSet(Collections.emptySet())
                .withProhibitedTimeslotTagSet(Collections.emptySet())
                .withUndesiredTimeslotTagSet(Collections.emptySet());
        LocalDateTime start1 = LocalDateTime.of(2018, 1, 1, 9, 0);
        LocalDateTime end1 = LocalDateTime.of(2018, 1, 1, 10, 0);
        Timeslot slot1 = new Timeslot(1L)
                .withTalkType(talkType)
                .withStartDateTime(start1)
                .withEndDateTime(end1);
        ConferenceSolution solution = new ConferenceSolution(1L)
                .withParametrization(parametrization)
                .withTalkList(Arrays.asList(talk1))
                .withTimeslotList(Arrays.asList(slot1))
                .withRoomList(Collections.emptyList())
                .withSpeakerList(Arrays.asList(speaker1, speaker2));
        parametrization.setSpeakerUndesiredTimeslotTag(1);
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_TIMESLOT_TAG, 0, solution);
        // talk with 1 speaker, speaker without undesired time slot tag
        slot1.setTagSet(Collections.emptySet());
        talk1.withSpeakerList(Arrays.asList(speaker1)).withTimeslot(slot1);
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_TIMESLOT_TAG, 0, solution);
        slot1.setTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_TIMESLOT_TAG, 0, solution);
        // talk with 1 speaker, speaker with undesired time slot tag, time slot without matching tag
        slot1.setTagSet(Collections.emptySet());
        speaker1.setUndesiredTimeslotTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_TIMESLOT_TAG, 0, solution);
        slot1.setTagSet(new HashSet<>(Arrays.asList(tag2, tag3)));
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_TIMESLOT_TAG, 0, solution);
        // talk with 1 speaker, speaker with undesired time slot tag, time slot with matching tag
        speaker1.setUndesiredTimeslotTagSet(new HashSet<>(Arrays.asList(tag1)));
        slot1.setTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_TIMESLOT_TAG, -1, solution);
        slot1.setTagSet(new HashSet<>(Arrays.asList(tag1, tag2)));
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_TIMESLOT_TAG, -1, solution);
        // talk with 1 speaker, speaker with undesired time slot tag, time slot with matching tag, weight = 2
        parametrization.setSpeakerUndesiredTimeslotTag(2);
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_TIMESLOT_TAG, -2, solution);
        // talk with 1 speaker, speaker with 2 undesired time slot tags
        parametrization.setSpeakerUndesiredTimeslotTag(1);
        speaker1.setUndesiredTimeslotTagSet(new HashSet<>(Arrays.asList(tag1, tag2)));
        slot1.setTagSet(Collections.emptySet());
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_TIMESLOT_TAG, 0, solution);
        slot1.setTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_TIMESLOT_TAG, -1, solution);
        slot1.setTagSet(new HashSet<>(Arrays.asList(tag1, tag2)));
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_TIMESLOT_TAG, -2, solution);
        // talk with 2 speakers
        talk1.withSpeakerList(Arrays.asList(speaker1, speaker2));
        slot1.setTagSet(Collections.emptySet());
        speaker1.setUndesiredTimeslotTagSet(new HashSet<>(Arrays.asList(tag1)));
        speaker2.setUndesiredTimeslotTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_TIMESLOT_TAG, 0, solution);
        slot1.setTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_TIMESLOT_TAG, -2, solution);
        speaker1.setUndesiredTimeslotTagSet(new HashSet<>(Arrays.asList(tag1, tag2)));
        speaker2.setUndesiredTimeslotTagSet(new HashSet<>(Arrays.asList(tag1, tag2)));
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_TIMESLOT_TAG, -2, solution);
        slot1.setTagSet(new HashSet<>(Arrays.asList(tag1, tag2)));
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_TIMESLOT_TAG, -4, solution);
        speaker1.setUndesiredTimeslotTagSet(new HashSet<>(Arrays.asList(tag1, tag2)));
        speaker2.setUndesiredTimeslotTagSet(new HashSet<>(Arrays.asList(tag1, tag3)));
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_TIMESLOT_TAG, -3, solution);
        slot1.setTagSet(new HashSet<>(Arrays.asList(tag1, tag2, tag3)));
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_TIMESLOT_TAG, -4, solution);
    }

    @Test
    public void talkUndesiredTimeslotTag() {
        ConferenceParametrization parametrization = new ConferenceParametrization(1L);
        String tag1 = "tag1";
        String tag2 = "tag2";
        String talkType = "type1";
        Talk talk1 = new Talk(1L)
                .withTalkType(talkType)
                .withSpeakerList(Collections.emptyList())
                .withPreferredTimeslotTagSet(Collections.emptySet())
                .withRequiredTimeslotTagSet(Collections.emptySet())
                .withProhibitedTimeslotTagSet(Collections.emptySet())
                .withUndesiredTimeslotTagSet(Collections.emptySet());
        LocalDateTime start1 = LocalDateTime.of(2018, 1, 1, 9, 0);
        LocalDateTime end1 = LocalDateTime.of(2018, 1, 1, 10, 0);
        Timeslot slot1 = new Timeslot(1L)
                .withTalkType(talkType)
                .withStartDateTime(start1)
                .withEndDateTime(end1);
        ConferenceSolution solution = new ConferenceSolution(1L)
                .withParametrization(parametrization)
                .withTalkList(Arrays.asList(talk1))
                .withTimeslotList(Arrays.asList(slot1))
                .withRoomList(Collections.emptyList())
                .withSpeakerList(Collections.emptyList());
        parametrization.setTalkUndesiredTimeslotTag(1);
        // talk without undesired time slot tags
        slot1.setTagSet(Collections.emptySet());
        talk1.withTimeslot(slot1);
        scoreVerifier.assertSoftWeight(TALK_UNDESIRED_TIMESLOT_TAG, 0, solution);
        slot1.setTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight("Talk required timeslot tag", 0, solution);
        // talk with undesired time slot tag, time slot without matching tag
        slot1.setTagSet(Collections.emptySet());
        talk1.withTimeslot(slot1).withUndesiredTimeslotTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(TALK_UNDESIRED_TIMESLOT_TAG, 0, solution);
        slot1.setTagSet(new HashSet<>(Arrays.asList(tag2)));
        scoreVerifier.assertSoftWeight(TALK_UNDESIRED_TIMESLOT_TAG, 0, solution);
        // talk with undesired time slot tag, time slot with matching tag
        talk1.withTimeslot(slot1).withUndesiredTimeslotTagSet(new HashSet<>(Arrays.asList(tag1)));
        slot1.setTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(TALK_UNDESIRED_TIMESLOT_TAG, -1, solution);
        slot1.setTagSet(new HashSet<>(Arrays.asList(tag1, tag2)));
        scoreVerifier.assertSoftWeight(TALK_UNDESIRED_TIMESLOT_TAG, -1, solution);
        // talk with undesired time slot tag, time slot with matching tag, weight = 2
        parametrization.setTalkUndesiredTimeslotTag(2);
        scoreVerifier.assertSoftWeight(TALK_UNDESIRED_TIMESLOT_TAG, -2, solution);
        // talk with 2 undesired time slot tags
        parametrization.setTalkUndesiredTimeslotTag(1);
        talk1.withTimeslot(slot1).withUndesiredTimeslotTagSet(new HashSet<>(Arrays.asList(tag1, tag2)));
        slot1.setTagSet(Collections.emptySet());
        scoreVerifier.assertSoftWeight(TALK_UNDESIRED_TIMESLOT_TAG, -0, solution);
        slot1.setTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(TALK_UNDESIRED_TIMESLOT_TAG, -1, solution);
        slot1.setTagSet(new HashSet<>(Arrays.asList(tag1, tag2)));
        scoreVerifier.assertSoftWeight(TALK_UNDESIRED_TIMESLOT_TAG, -2, solution);
    }

    @Test
    public void speakerPreferredRoomTag() {
        ConferenceParametrization parametrization = new ConferenceParametrization(1L);
        String tag1 = "tag1";
        String tag2 = "tag2";
        Room room1 = new Room(1L);
        Talk talk1 = new Talk(1L)
                .withSpeakerList(Collections.emptyList())
                .withPreferredTimeslotTagSet(Collections.emptySet())
                .withRequiredTimeslotTagSet(Collections.emptySet())
                .withRequiredRoomTagSet(Collections.emptySet())
                .withPreferredRoomTagSet(Collections.emptySet())
                .withProhibitedRoomTagSet(Collections.emptySet())
                .withUndesiredRoomTagSet(Collections.emptySet());
        Speaker speaker1 = new Speaker(1L)
                .withUnavailableTimeslotSet(Collections.emptySet())
                .withRequiredTimeslotTagSet(Collections.emptySet())
                .withPreferredTimeslotTagSet(Collections.emptySet())
                .withRequiredRoomTagSet(Collections.emptySet())
                .withPreferredRoomTagSet(Collections.emptySet())
                .withProhibitedRoomTagSet(Collections.emptySet())
                .withUndesiredRoomTagSet(Collections.emptySet());
        Speaker speaker2 = new Speaker(2L)
                .withUnavailableTimeslotSet(Collections.emptySet())
                .withRequiredTimeslotTagSet(Collections.emptySet())
                .withPreferredTimeslotTagSet(Collections.emptySet())
                .withRequiredRoomTagSet(Collections.emptySet())
                .withPreferredRoomTagSet(Collections.emptySet())
                .withProhibitedRoomTagSet(Collections.emptySet())
                .withUndesiredRoomTagSet(Collections.emptySet());
        ConferenceSolution solution = new ConferenceSolution(1L)
                .withParametrization(parametrization)
                .withTalkList(Arrays.asList(talk1))
                .withTimeslotList(Collections.emptyList())
                .withRoomList(Arrays.asList(room1))
                .withSpeakerList(Arrays.asList(speaker1, speaker2));
        parametrization.setSpeakerPreferredRoomTag(1);
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_ROOM_TAG, 0, solution);
        // talk with 1 speaker, speaker without preferred room tag
        room1.setTagSet(Collections.emptySet());
        talk1.withSpeakerList(Arrays.asList(speaker1)).withRoom(room1);
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_ROOM_TAG, 0, solution);
        // talk with 1 speaker, speaker with preferred room tag, room without matching tag
        speaker1.withPreferredRoomTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_ROOM_TAG, -1, solution);
        room1.setTagSet(new HashSet<>(Arrays.asList(tag2)));
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_ROOM_TAG, -1, solution);
        // talk with 1 speaker, speaker with preferred room tag, room without matching tag, weight = 2
        parametrization.setSpeakerPreferredRoomTag(2);
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_ROOM_TAG, -2, solution);
        // talk with 1 speaker, speaker with required room tag, room with matching tag
        parametrization.setSpeakerPreferredRoomTag(1);
        room1.setTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_ROOM_TAG, 0, solution);
        // talk with 1 speaker, speaker with 2 required room tags
        speaker1.withPreferredRoomTagSet(new HashSet<>(Arrays.asList(tag1, tag2)));
        room1.setTagSet(Collections.emptySet());
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_ROOM_TAG, -2, solution);
        room1.setTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_ROOM_TAG, -1, solution);
        room1.setTagSet(new HashSet<>(Arrays.asList(tag1, tag2)));
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_ROOM_TAG, 0, solution);
        // talk with 2 speakers
        talk1.withSpeakerList(Arrays.asList(speaker1, speaker2));
        speaker1.withPreferredRoomTagSet(new HashSet<>(Arrays.asList(tag1)));
        speaker2.withPreferredRoomTagSet(new HashSet<>(Arrays.asList(tag1)));
        room1.setTagSet(Collections.emptySet());
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_ROOM_TAG, -2, solution);
        room1.setTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_ROOM_TAG, 0, solution);
        speaker2.withPreferredRoomTagSet(new HashSet<>(Arrays.asList(tag2)));
        room1.setTagSet(Collections.emptySet());
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_ROOM_TAG, -2, solution);
        room1.setTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_ROOM_TAG, -1, solution);
        room1.setTagSet(new HashSet<>(Arrays.asList(tag1, tag2)));
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_ROOM_TAG, 0, solution);
        speaker2.withPreferredRoomTagSet(new HashSet<>(Arrays.asList(tag1, tag2)));
        room1.setTagSet(Collections.emptySet());
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_ROOM_TAG, -3, solution);
        room1.setTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_ROOM_TAG, -1, solution);
        room1.setTagSet(new HashSet<>(Arrays.asList(tag1, tag2)));
        scoreVerifier.assertSoftWeight(SPEAKER_PREFERRED_ROOM_TAG, 0, solution);
    }

    @Test
    public void talkPreferredRoomTag() {
        ConferenceParametrization parametrization = new ConferenceParametrization(1L);
        String tag1 = "tag1";
        String tag2 = "tag2";
        String talkType = "type1";
        Room room1 = new Room(1L);
        Talk talk1 = new Talk(1L)
                .withTalkType(talkType)
                .withSpeakerList(Collections.emptyList())
                .withPreferredTimeslotTagSet(Collections.emptySet())
                .withRequiredTimeslotTagSet(Collections.emptySet())
                .withPreferredRoomTagSet(Collections.emptySet())
                .withRequiredRoomTagSet(Collections.emptySet())
                .withProhibitedRoomTagSet(Collections.emptySet())
                .withUndesiredRoomTagSet(Collections.emptySet());
        ConferenceSolution solution = new ConferenceSolution(1L)
                .withParametrization(parametrization)
                .withTalkList(Arrays.asList(talk1))
                .withTimeslotList(Collections.emptyList())
                .withRoomList(Collections.emptyList())
                .withSpeakerList(Collections.emptyList());
        parametrization.setTalkPreferredRoomTag(1);
        scoreVerifier.assertSoftWeight(TALK_PREFERRED_ROOM_TAG, 0, solution);
        // talk without preferred room tags
        room1.setTagSet(Collections.emptySet());
        talk1.withRoom(room1);
        scoreVerifier.assertSoftWeight(TALK_PREFERRED_ROOM_TAG, 0, solution);
        room1.setTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(TALK_PREFERRED_ROOM_TAG, 0, solution);
        // talk with preferred room tag, room without matching tag
        talk1.withPreferredRoomTagSet(new HashSet<>(Arrays.asList(tag1)));
        room1.setTagSet(Collections.emptySet());
        scoreVerifier.assertSoftWeight(TALK_PREFERRED_ROOM_TAG, -1, solution);
        room1.setTagSet(new HashSet<>(Arrays.asList(tag2)));
        scoreVerifier.assertSoftWeight(TALK_PREFERRED_ROOM_TAG, -1, solution);
        // talk with preferred room tag, room without matching tag, weight = 2
        parametrization.setTalkPreferredRoomTag(2);
        scoreVerifier.assertSoftWeight(TALK_PREFERRED_ROOM_TAG, -2, solution);
        // talk with preferred room tag, room with matching tag
        parametrization.setTalkPreferredRoomTag(1);
        room1.setTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(TALK_PREFERRED_ROOM_TAG, 0, solution);
        // talk with two preferred room tags
        room1.setTagSet(Collections.emptySet());
        talk1.withPreferredRoomTagSet(new HashSet<>(Arrays.asList(tag1, tag2)));
        scoreVerifier.assertSoftWeight(TALK_PREFERRED_ROOM_TAG, -2, solution);
        room1.setTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(TALK_PREFERRED_ROOM_TAG, -1, solution);
        room1.setTagSet(new HashSet<>(Arrays.asList(tag1, tag2)));
        scoreVerifier.assertSoftWeight(TALK_PREFERRED_ROOM_TAG, 0, solution);
    }

    @Test
    public void speakerUndesiredRoomTag() {
        ConferenceParametrization parametrization = new ConferenceParametrization(1L);
        String tag1 = "tag1";
        String tag2 = "tag2";
        String tag3 = "tag3";
        Room room1 = new Room(1L);
        Talk talk1 = new Talk(1L)
                .withSpeakerList(Collections.emptyList())
                .withPreferredTimeslotTagSet(Collections.emptySet())
                .withRequiredTimeslotTagSet(Collections.emptySet())
                .withRequiredRoomTagSet(Collections.emptySet())
                .withPreferredRoomTagSet(Collections.emptySet())
                .withProhibitedRoomTagSet(Collections.emptySet())
                .withUndesiredRoomTagSet(Collections.emptySet());
        Speaker speaker1 = new Speaker(1L)
                .withUnavailableTimeslotSet(Collections.emptySet())
                .withRequiredTimeslotTagSet(Collections.emptySet())
                .withRequiredRoomTagSet(Collections.emptySet())
                .withPreferredTimeslotTagSet(Collections.emptySet())
                .withPreferredRoomTagSet(Collections.emptySet())
                .withProhibitedRoomTagSet(Collections.emptySet())
                .withUndesiredRoomTagSet(Collections.emptySet());
        Speaker speaker2 = new Speaker(2L)
                .withUnavailableTimeslotSet(Collections.emptySet())
                .withRequiredTimeslotTagSet(Collections.emptySet())
                .withRequiredRoomTagSet(Collections.emptySet())
                .withPreferredTimeslotTagSet(Collections.emptySet())
                .withPreferredRoomTagSet(Collections.emptySet())
                .withProhibitedRoomTagSet(Collections.emptySet())
                .withUndesiredRoomTagSet(Collections.emptySet());
        ConferenceSolution solution = new ConferenceSolution(1L)
                .withParametrization(parametrization)
                .withTalkList(Arrays.asList(talk1))
                .withTimeslotList(Collections.emptyList())
                .withRoomList(Arrays.asList(room1))
                .withSpeakerList(Arrays.asList(speaker1, speaker2));
        parametrization.setSpeakerUndesiredRoomTag(1);
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_ROOM_TAG, 0, solution);
        // talk with 1 speaker without undesired room tags
        room1.setTagSet(Collections.emptySet());
        talk1.withSpeakerList(Arrays.asList(speaker1)).withRoom(room1);
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_ROOM_TAG, 0, solution);
        // talk with 1 speaker, speaker with undesired room tag, room without matching tag
        speaker1.withUndesiredRoomTagSet(new HashSet<>(Arrays.asList(tag1)));
        room1.setTagSet(Collections.emptySet());
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_ROOM_TAG, 0, solution);
        room1.setTagSet(new HashSet<>(Arrays.asList(tag3)));
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_ROOM_TAG, 0, solution);
        // talk with 1 speaker, speaker with undesired room tag, room with matching tag
        speaker1.withUndesiredRoomTagSet(new HashSet<>(Arrays.asList(tag1)));
        room1.setTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_ROOM_TAG, -1, solution);
        // talk with 1 speaker, speaker with undesired room tag, room with matching tag, weight = 2
        parametrization.setSpeakerUndesiredRoomTag(2);
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_ROOM_TAG, -2, solution);
        // talk with 1 speaker, speaker with 2 undesired room tags
        parametrization.setSpeakerUndesiredRoomTag(1);
        speaker1.withUndesiredRoomTagSet(new HashSet<>(Arrays.asList(tag1, tag2)));
        room1.setTagSet(Collections.emptySet());
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_ROOM_TAG, 0, solution);
        room1.setTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_ROOM_TAG, -1, solution);
        room1.setTagSet(new HashSet<>(Arrays.asList(tag1, tag2)));
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_ROOM_TAG, -2, solution);
        // talk with 2 speakers
        talk1.withSpeakerList(Arrays.asList(speaker1, speaker2)).withRoom(room1);
        speaker1.withUndesiredRoomTagSet(new HashSet<>(Arrays.asList(tag1)));
        speaker2.withUndesiredRoomTagSet(new HashSet<>(Arrays.asList(tag1)));
        room1.setTagSet(Collections.emptySet());
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_ROOM_TAG, 0, solution);
        room1.setTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_ROOM_TAG, -2, solution);
        speaker2.withUndesiredRoomTagSet(new HashSet<>(Arrays.asList(tag2)));
        room1.setTagSet(Collections.emptySet());
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_ROOM_TAG, 0, solution);
        room1.setTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_ROOM_TAG, -1, solution);
        room1.setTagSet(new HashSet<>(Arrays.asList(tag1, tag2)));
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_ROOM_TAG, -2, solution);
        speaker2.withUndesiredRoomTagSet(new HashSet<>(Arrays.asList(tag1, tag2)));
        room1.setTagSet(Collections.emptySet());
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_ROOM_TAG, 0, solution);
        room1.setTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_ROOM_TAG, -2, solution);
        room1.setTagSet(new HashSet<>(Arrays.asList(tag1, tag2)));
        scoreVerifier.assertSoftWeight(SPEAKER_UNDESIRED_ROOM_TAG, -3, solution);
    }

    @Test
    public void talkUndesiredRoomTag() {
        ConferenceParametrization parametrization = new ConferenceParametrization(1L);
        String tag1 = "tag1";
        String tag2 = "tag2";
        String talkType = "type1";
        Room room1 = new Room(1L);
        Talk talk1 = new Talk(1L)
                .withTalkType(talkType)
                .withSpeakerList(Collections.emptyList())
                .withPreferredTimeslotTagSet(Collections.emptySet())
                .withRequiredTimeslotTagSet(Collections.emptySet())
                .withPreferredRoomTagSet(Collections.emptySet())
                .withRequiredRoomTagSet(Collections.emptySet())
                .withProhibitedRoomTagSet(Collections.emptySet())
                .withUndesiredRoomTagSet(Collections.emptySet());
        ConferenceSolution solution = new ConferenceSolution(1L)
                .withParametrization(parametrization)
                .withTalkList(Arrays.asList(talk1))
                .withTimeslotList(Collections.emptyList())
                .withRoomList(Collections.emptyList())
                .withSpeakerList(Collections.emptyList());
        parametrization.setTalkUndesiredRoomTag(1);
        // talk without undesired room tags
        room1.setTagSet(Collections.emptySet());
        talk1.withRoom(room1);
        scoreVerifier.assertSoftWeight(TALK_UNDESIRED_ROOM_TAG, 0, solution);
        room1.setTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(TALK_UNDESIRED_ROOM_TAG, 0, solution);
        // talk with undesired room tag, room without matching tag
        talk1.withUndesiredRoomTagSet(new HashSet<>(Arrays.asList(tag1)));
        room1.setTagSet(Collections.emptySet());
        scoreVerifier.assertSoftWeight(TALK_UNDESIRED_ROOM_TAG, 0, solution);
        room1.setTagSet(new HashSet<>(Arrays.asList(tag2)));
        scoreVerifier.assertSoftWeight(TALK_UNDESIRED_ROOM_TAG, 0, solution);
        // talk with undesired room tag, room with matching tag
        talk1.withUndesiredRoomTagSet(new HashSet<>(Arrays.asList(tag1)));
        room1.setTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(TALK_UNDESIRED_ROOM_TAG, -1, solution);
        // talk with undesired room tag, room with matching tag, weight = 2
        parametrization.setTalkUndesiredRoomTag(2);
        scoreVerifier.assertSoftWeight(TALK_UNDESIRED_ROOM_TAG, -2, solution);
        // talk with 2 undesired room tags
        parametrization.setTalkUndesiredRoomTag(1);
        room1.setTagSet(Collections.emptySet());
        talk1.withUndesiredRoomTagSet(new HashSet<>(Arrays.asList(tag1, tag2)));
        scoreVerifier.assertSoftWeight(TALK_UNDESIRED_ROOM_TAG, 0, solution);
        room1.setTagSet(new HashSet<>(Arrays.asList(tag1)));
        scoreVerifier.assertSoftWeight(TALK_UNDESIRED_ROOM_TAG, -1, solution);
        room1.setTagSet(new HashSet<>(Arrays.asList(tag1, tag2)));
        scoreVerifier.assertSoftWeight(TALK_UNDESIRED_ROOM_TAG, -2, solution);
    }

}
