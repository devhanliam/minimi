import {Box, Container, CssBaseline, Grid, Toolbar} from "@material-ui/core";
import Post from "./Post";
import MyPostForm from "./MyPostForm";

const MyPost = ()=>{
    return(
        <Box sx={{display:'flex'}} maxWidth="lg">
            <CssBaseline/>
            <Toolbar/>
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
                        <MyPostForm/>
                    </Container>
                </Box>
            </main>
        </Box>
    )


}

export default MyPost;