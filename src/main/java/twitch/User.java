package twitch;

public class User implements MessagingChannel {

    private String name;

    public User(String name){
        this.name = name;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String getTargetAddress() {
        return name;
    }
}
