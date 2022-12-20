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
import {useInView} from "react-intersection-observer";

const Main = () => {
    const [postList,setPostList] = useState([]);
    const [ref, inView] = useInView();
    const [cursorId, setCursorId] = useState(null);
    const [loading, setLoading] = useState(false);
    const [renderFlag, setRenderFlag] = useState(true);
    const [listSize, setListSize] = useState(0)
    const getPostList = () => {
        let param = cursorId ? `?cursorId=${cursorId}`:'';
        setLoading(true);
        callApi(`/api/v1/post/list${param}`,"get",null)
            .then(function (response){
                let data = response.data.postInfoFormList;
                let postData = [];
                    console.log(data);
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

                setListSize(postData.length);
                setPostList(prevState => [...prevState,...postData]);
                setCursorId(response.data.cursorId);
            })
            .catch(error => {


            });
        setLoading(false)
    }


    useEffect(()=>{
        if(renderFlag){
            getPostList();
        }
        setRenderFlag(false);
    },[renderFlag]);

    useEffect(()=>{
        if (listSize > 0 && inView && !loading) {
            setRenderFlag(true);
        }
    },[inView])
    return (
        <Box sx={{}} maxWidth="lg">
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
                            {postList.map((p,idx,) => (
                                        <Post key={p.id} post={p}/>
                            ))}
                            <div ref={ref} />
                        </Grid>
                    </Container>
                </Box>
            </main>
        </Box>

    );
}

export default Main;