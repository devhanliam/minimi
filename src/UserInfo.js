import {Box, Container, CssBaseline, Toolbar} from "@material-ui/core";
import MyPostForm from "./MyPostForm";
import UserInfoForm from "./UserInfoForm";

const UserInfo = () => {
    return(
        <Box sx={{}} maxWidth="lg">
            <CssBaseline/>
            <Toolbar/>
            {/*<Header/>*/}
            <main>
                <Box component='main'>
                    <Toolbar/>
                    <Container maxWidth="md">
                        <UserInfoForm/>
                    </Container>
                </Box>
            </main>
        </Box>
    )


}
export default UserInfo;