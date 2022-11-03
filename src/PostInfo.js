import React, {Fragment, useEffect, useState} from "react";
import {
    Box,
    Button,
    Container,
    CssBaseline,
    Divider,
    IconButton,
    InputAdornment,
    TextField,
    Toolbar
} from "@material-ui/core";
import Header from "./Header";
import InfoForm from "./InfoForm";
import {useParams} from "react-router-dom";
import {callApi} from "./axiosUtils";
import Comment from "./Comment";
import {Textsms} from "@material-ui/icons";

const PostInfo = () => {
    const [postInfo, setPostInfo] = useState({});
    const [commentList, setCommentList] = useState([]);
    const [editFlag, setEditFlag] = useState(false);
    const { id } = useParams();
    const setPostData = (response)=>{
        setPostInfo({
            "title": response.data.title,
            "views": response.data.views,
            "writeTime": response.data.writeTime,
            "writer": response.data.writer,
            "content": response.data.content,
            "file": response.data.file.length > 0 ? `/api/v1/post/image/${response.data.file[0].fileName}` : ""
        });
        console.log(response.data.commentList);
        setCommentList(response.data.commentList);

    };
    useEffect(()=>{
         callApi(`/api/v1/post/info/${id}`, "get")
            .then(function (response) {
                console.log(response);
                setPostData(response);
                if (localStorage.getItem("email") === response.data.email) {
                    setEditFlag(true);
                }
            });
    },[])
    const insertComment= ()=>{
        let content = document.querySelector("#comment");
        if(content.value.length > 200){
            alert("200자까지 작성 가능합니다");
            return;
        }
        let data = {
            "email": localStorage.getItem("email"),
            "boardId" : id,
            // "commentId" : commentId
            "content": content.value
        }

        callApi("/api/v1/user/post/comment/create", "post", data)
            .then(response => {
                alert("댓글이 등록되었습니다.")
                content.value = "";
                callApi(`/api/v1/post/info/${id}`, "get")
                    .then(function (response) {
                        console.log(response);
                        setPostData(response);
                    });
            })
            .catch(function (error) {
                if(Array.isArray(error.response.data)){
                    for (let i = 0; i < error.response.data.length; i++) {
                        alert(error.response.data[i].message);
                    }
                }else{
                    console.log(error);
                    alert(error.response.data.message);
                }
            })

    };
    return(
        <Fragment>
            <Toolbar/>
            <Box sx={{display:'flex'}} maxWidth="lg">
                {/*<Header/>*/}
                <CssBaseline/>
                <main style={{marginBottom: 100}}>
                    <Box component='main'
                         // sx={{
                         //     flexGrow: 1,
                         //     height: '100vh',
                         //     overflow: 'auto',
                         // }}
                         // style={{
                         //     flexGrow: 1,
                         //     height: '100vh',
                         //     overflow: 'auto',
                         // }}
                    >
                        <Toolbar/>
                        <Container maxWidth="lg">
                            <InfoForm postInfo={postInfo} editFlag={editFlag} id={id}/>
                            <Divider/>
                            <TextField
                                        id="comment"
                                        fullWidth
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
                            {
                               commentList.map((c,idx)=>(
                                    <Comment key={idx} comment={c}/>
                                ))
                            }
                        </Container>
                    </Box>
                </main>
            </Box>
        </Fragment>
    );
}

export default PostInfo;