package com.al3x.housing2.Data;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResourcePackData {
    private String id;
    private String prompt;
    private boolean force;

    public ResourcePackData() {
    }

    public ResourcePackData(String id, String prompt, boolean force) {
        this.id = id;
        this.prompt = prompt;
        this.force = force;
    }

}