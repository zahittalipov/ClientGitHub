package com.angelectro.zahittalipov.clientgithub.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Zahit Talipov on 24.11.2015.
 */
public class Commit {
    @SerializedName("sha")
    private String sha;
    @SerializedName("commit")
    private ItemCommit commit;

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }


    public ItemCommit getCommit() {
        return commit;
    }

    public void setCommit(ItemCommit commit) {
        this.commit = commit;
    }


}
