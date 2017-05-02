package com.szmg.grafana.domain.panel;

import com.szmg.grafana.domain.Panel;

/**
 * https://github.com/grafana/grafana/blob/master/public/app/plugins/panel/text/module.ts
 */
public class Text extends Panel {

    private Mode mode = Mode.markdown;
    private String content = "# title";

    @Override
    public String getType() {
        return "text";
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public enum Mode {
        html, markdown, text
    }
}
