import {Fragment} from "react";
import {Box, Container, CssBaseline, Toolbar} from "@material-ui/core";
import UpdateForm from "./UpdateForm";

const PostUpdate = () => {

    return(
        <Fragment>
            <Toolbar/>
            <Box sx={{display:'flex'}} maxWidth="lg">
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
                            <UpdateForm/>
                        </Container>
                    </Box>
                </main>
            </Box>
        </Fragment>
    );
}
export default PostUpdate;