package twitch;

import java.util.LinkedList;
import java.util.List;

/**
 * TODO: rework into TO structure (think about if that is actually best way to model)
 */
public class ChannelTo {
    private User owner;
    private List<User> allUsers; // might have discard this and use db calls later if list too long
    private List<User> currentViewers;
    private boolean joined;
    private String name;



    public void setViewers(List<User> currentViewers){
        if(!joined)
        this.currentViewers = currentViewers != null ? currentViewers
                : new LinkedList<>();
    }

    public List<User> getCurrentViewers(){
        return currentViewers;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}
