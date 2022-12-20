import Header from "./Header";
import {Box, Button, Container, CssBaseline, Toolbar} from "@material-ui/core";
import EditForm from "./EditForm";
import {Fragment} from "react";

const PostEdit = ()=>{

    return(
        <Fragment>
        <Toolbar/>
        <Box sx={{}} maxWidth="lg">
            {/*<Header/>*/}
            <CssBaseline/>
            <main>
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
                        <EditForm/>
                    </Container>
                </Box>
            </main>
        </Box>
        </Fragment>
    );
}

export default PostEdit;