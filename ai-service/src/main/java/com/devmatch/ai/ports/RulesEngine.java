package com.devmatch.ai.ports;

import com.devmatch.ai.domain.MatchCandidate;
import com.devmatch.ai.domain.MatchResult;
import java.util.List;

/*
  Check Engine Rules (skills, language, geo)
*/
public interface RulesEngine {
  List<MatchResult> rescoreUserToVacancies(String userId, List<MatchCandidate> initial);
}
