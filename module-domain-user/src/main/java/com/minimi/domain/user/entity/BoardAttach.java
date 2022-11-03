package com.minimi.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardAttach extends BaseEntity{

    @Id
    @Column(name = "attach_id")
    @GeneratedValue
    private Long id;
    private String filePath;
    private String fileName;
    private String originalName;
    private Long fileSize;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public void setBoard(Board board){
        if(this.board !=null){
            this.board.getAttachList().remove(this);
        }
        this.board = board;
        if (!board.getAttachList().contains(this)) {
            board.getAttachList().add(this);
        }
    }
}
