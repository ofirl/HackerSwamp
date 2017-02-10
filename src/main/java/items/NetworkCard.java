package items;

public class NetworkCard extends  BaseItem{
    // public variables
    public int downloadLimit;
    public int uploadLimit;

    /**
     * constructor
     */
    public NetworkCard(int id, String name, int downloadLimit, int uploadLimit) {
        super(id, name);
        this.downloadLimit = downloadLimit;
        this.uploadLimit = uploadLimit;
    }
}
