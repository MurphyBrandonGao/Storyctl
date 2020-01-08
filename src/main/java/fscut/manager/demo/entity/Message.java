package fscut.manager.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import fscut.manager.demo.entity.UPK.StoryUPK;
import lombok.Data;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.util.Date;

@Entity
@Indexed
@Data
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer messageId;

    private StoryUPK storyUPK;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdTime;


}
