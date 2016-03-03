/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

import java.util.HashMap;
import java.util.Map;

public class PollResponse {
    Map<String, String> answerPercent;

    public Map<String, String> getAnswerPercent() {
        return answerPercent;
    }

    public void setAnswerPercent(HashMap<String, String> answerPercent) {
        this.answerPercent = answerPercent;
    }


}
