import {
    Box,
    Container,
    CssBaseline,
    Grid, Toolbar,
} from "@material-ui/core";
import Header from "./Header";
import Post from "./Post";
import {useEffect, useState} from "react";
import {callApi, IMG_SRC_HOST} from "./axiosUtils";

const Main = () => {
    const [postList,setPostList] = useState([]);
    useEffect(()=>{
        callApi("/api/v1/post/list","get",null)
            .then(function (response){
                console.log(response);
                let data = response.data;
                let postData = [];
                if (Array.isArray(data)) {
                    for (let i = 0; i < data.length; i++) {
                        let postInfo = {
                            'date' : data[i].writeTime,
                            'content' : data[i].content,
                            'title' : data[i].title,
                            'image' : data[i].file.length > 0?
                                `/api/v1/post/image/${data[i].file[0].fileName}`:'',
                            'imageLabel' : data[i].file.length > 0? data[i].file[0].fileName:'none',
                            'writer' : data[i].writer,
                            'id': data[i].postId,
                            'views':data[i].views
                        }
                        postData[i] = postInfo;
                    }
                }
                setPostList(postData);
            });
    },[]);
    return (
        <Box sx={{display:'flex'}} maxWidth="lg">
            <CssBaseline/>
            {/*<Header/>*/}
            <main>
                <Box component='main'
                     sx={{
                         flexGrow: 1,
                     height: '100vh',
                     overflow: 'auto',
                }}
                >
                    <Toolbar/>
                    <Container maxWidth="lg">
                        <Grid container spacing={2}>
                            {postList.map((p,idx) => (
                                <Post key={idx} post={p}/>
                            ))}
                        </Grid>
                    </Container>
                </Box>
            </main>
        </Box>

    );
}

export default Main;