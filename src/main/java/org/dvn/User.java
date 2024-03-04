package org.dvn;

import lombok.Setter;

public class User {

    private String name;
    private String surname;
    private String nickname;

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {

        private User user;

        private UserBuilder() {
            user = new User();
        }

        public UserBuilder name(String name) {
            user.setName(name);
            return this;
        }

        public UserBuilder surname(String surname) {
            user.setSurname(surname);
            return this;
        }

        public UserBuilder nickname(String nickname) {
            user.setNickname(nickname);
            return this;
        }


        public User build() {
            return user;
        }

    }

}
