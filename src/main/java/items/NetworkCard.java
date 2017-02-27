package items;

import java.util.HashMap;

public class NetworkCard extends  BaseItem{
    // public variables
    public int downloadLimit;
    public int uploadLimit;

    /**
     * constructor
     */
    public NetworkCard(int id, String name, int price, int downloadLimit, int uploadLimit) {
        super(id, name, price);
        this.downloadLimit = downloadLimit;
        this.uploadLimit = uploadLimit;
    }

    /**
     * gets the network card as arguments to send
     * @return the network card as arguments
     */
    public HashMap<String, String> getSpecAsArguments() {
        HashMap<String, String> networkCard = new HashMap<>();

        networkCard.put("network_card_id", String.valueOf(id));
        networkCard.put("network_card_name", name);
        networkCard.put("network_card_download", String.valueOf(downloadLimit));
        networkCard.put("network_card_upload", String.valueOf(uploadLimit));

        return networkCard;
    }

    /**
     * detailed info about the item
     */
    @Override
    public String toString() {
        String output = "";

        output += super.toString() + "\n";
        output += "Upload limit : " + uploadLimit + " Mb/s \n";
        output += "Download limit : " + downloadLimit + " Mb/s";

        return output;
    }
}
