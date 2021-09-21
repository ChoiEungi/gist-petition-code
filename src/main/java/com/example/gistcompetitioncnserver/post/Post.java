package com.example.gistcompetitioncnserver.post;

import com.example.gistcompetitioncnserver.comment.Comment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Post {

    @Id
    @GeneratedValue(
            strategy= GenerationType.AUTO,
            generator="native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    @Column(name = "postId")
    private Long id;

    private String title;

    private String description;

    private String category;

    private String created;

    private boolean answered;

    private int accepted;

    private Long userId;

    @OneToMany(mappedBy = "post")
    private final List<Comment> comment = new ArrayList<>();

    public Post() {
    }

    //    //foreign key
//    @ManyToOne
//    @JoinColumn(name = "id")
//    private User user;


}
