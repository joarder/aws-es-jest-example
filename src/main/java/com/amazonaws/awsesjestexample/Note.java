/*
 * Copyright 2018 Joarder Kamal. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may
 * not use this file except in compliance with the License. A copy of the
 * License is located at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.amazonaws.awsesjestexample;

import io.searchbox.annotations.JestId;

import java.io.Serializable;
import java.util.Date;

public class Note implements Serializable {
    private static final long serialVersionUID = -3971912226293959387L;

    @JestId
    private String id;

    private String note;

    private Date createdOn;

    private String userName;

    public Note(final String userName, final String note) {
        this.userName = userName;
        this.note = note;
        this.createdOn = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "Note [id=" + id + ", note=" + note + ", createdOn=" + createdOn
                + ", userName=" + userName + "]";
    }

}
