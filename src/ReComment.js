import Comment from "./Comment";
import {Avatar, Box, Button, Divider, Grid, InputAdornment, Paper, TextField, Typography} from "@material-ui/core";
import {Textsms} from "@material-ui/icons";
import React, {useState} from "react";
import {useParams} from "react-router-dom";
import {callApi} from "./axiosUtils";

const ReComment = (props) =>{
    const {comment} = props;
    const [displayFlag, setDisplayFlag] = useState(false);
    const {id} = useParams();
    const insertComment = ()=>{
        let content = document.querySelector('#reply' + props.id);
        if(content.value.length > 200){
            alert("200자까지 작성 가능합니다");
            return;
        }
        let data = {
            'commentId': props.id,
            'boardId': id,
            'content': content.value
        };
        callApi("/api/v1/user/post/re-comment/create", "post",data)
            .then(res => {
                alert("댓글이 작성됐습니다");
                content.value='';
                callApi(`/api/v1/post/info/${id}`, "get")
                    .then(function (response) {
                        console.log(response);
                        props.setCommentList(response.data.commentList);
                    });
            })
            .catch(err => {
                console.log(err);
                alert("댓글 작성에 실패했습니다");
            });

    }
    return (
        <Box style={{marginLeft:10}}>
            <Divider/>
            <br/>
            <Grid container wrap="nowrap" spacing={2}>
                <Grid item>
                    <Avatar>{comment.nickName}</Avatar>
                </Grid>
                <Grid  item xs zeroMinWidth>
                    <Typography variant="h4">
                        {comment.nickName}
                    </Typography>
                    <Typography variant="body1" style={{textAlign:"left"}}>
                        {comment.content}
                    </Typography>
                    <Button color="primary" onClick={()=>{setDisplayFlag(!displayFlag)}}>답글</Button>
                    {localStorage.getItem("email")?
                        <TextField
                            id={`reply${props.id}`}
                            fullWidth
                            style={{display:displayFlag?"":"none"}}
                            multiline
                            placeholder="공감의 이야기를 남겨주세요"
                            InputProps={{

                                startAdornment: (
                                    <InputAdornment position="start">
                                        <Textsms />
                                    </InputAdornment>
                                ),
                                endAdornment:(
                                    <InputAdornment position="end">
                                        <Button color="primary" onClick={insertComment}>입력</Button>
                                    </InputAdornment>

                                )
                            }}
                            variant="standard"
                        />

                        :

                        <TextField
                            style={{display:displayFlag?"":"none"}}
                            fullWidth
                            multiline
                            placeholder="로그인 후 이야기를 나눠보세요"
                            InputProps={{
                                readOnly : true,
                                startAdornment: (
                                    <InputAdornment position="start">
                                        <Textsms />
                                    </InputAdornment>
                                ),
                                endAdornment:(
                                    <InputAdornment position="end">
                                        <Button color="primary" onClick={()=>{window.location.href="/login"}}>로그인</Button>
                                    </InputAdornment>

                                )
                            }}
                            variant="standard"
                        />

                    }
                </Grid>
            </Grid>
            {
                comment.children.map(c =>
                    <ReComment key={c.id} id={c.id} comment={c} setCommentList={props.setCommentList}/>
                )
            }
        </Box>


    );

}
export default ReComment