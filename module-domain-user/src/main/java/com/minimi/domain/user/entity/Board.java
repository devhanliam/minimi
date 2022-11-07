package com.minimi.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
public class Board extends BaseEntity{

    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String content;
    private boolean openFlag;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    @Builder.Default
    @OneToMany(mappedBy = "board",cascade = {CascadeType.REMOVE})
    private List<Comment> commentList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "board",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<BoardAttach> attachList = new ArrayList<>();

    @Column(columnDefinition = "integer default 0",nullable = false)
    private Long views;

//    public void addBoardAttach(BoardAttach boardAttach) {
//        this.attachList.add(boardAttach);
//        if(boardAttach.getBoard() != null){
//            boardAttach.bio;
//        }
//    }
    public void increaseViews(){
        this.views ++;
    }
    public void setUpdateValue(String title, String content){
        this.title = title;
        this.content = content;
    }


}
