import {Box, Container, CssBaseline, Grid, Toolbar} from "@material-ui/core";
import Post from "./Post";
import MyPostForm from "./MyPostForm";

const MyPost = ()=>{
    return(
        <Box sx={{}} maxWidth="lg">
            <CssBaseline/>
            <Toolbar/>
            {/*<Header/>*/}
            <main>
                <Box component='main'

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