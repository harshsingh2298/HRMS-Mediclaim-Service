package cms.cdl.com.mailingService.mailingService.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
public class IdeaLikeRequest {
    private String likerEcode;
    private String likerName;


    public IdeaLikeRequest(String likerEcode, String likerName) {
        this.likerEcode = likerEcode;
        this.likerName = likerName;
    }

    public String getLikerEcode() {
        return likerEcode;
    }

    public void setLikerEcode(String likerEcode) {
        this.likerEcode = likerEcode;
    }

    public String getLikerName() {
        return likerName;
    }

    public void setLikerName(String likerName) {
        this.likerName = likerName;
    }
}
