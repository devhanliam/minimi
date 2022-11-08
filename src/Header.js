import {
    AppBar,
    Box,
    Button,
    CssBaseline, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle,
    Drawer,
    Grid,
    IconButton,
    makeStyles, Slide,
    Toolbar, Tooltip,
    Typography
} from "@material-ui/core";
import {Edit, ExitToApp, Input, MenuRounded, VpnKey, VpnLock} from "@material-ui/icons";
import React,{Fragment, useState} from "react";
import instance, {callApi} from "./axiosUtils";
import SideMenu from "./SideMenu";

const useStyles = makeStyles(() => ({
    typographyStyles :{
        flex : 1
    }
}));
const Header = () => {
    const [sideMenuFlag,setSideMenuFlag] = useState(false);
    const [isLogin, setIsLogin] = useState(false);
    const [isLoginModalOpen, setIsLoginModalOpen] = useState(false);
    const postButtonAction = ()=>{
        if (isLogin) {
            window.location.href = "/post";
        } else {
            setIsLoginModalOpen(true);
        }
    }
    const classes = useStyles();
    if(localStorage.getItem('accessToken')){
        instance()
            .then((response)=>{
                if (response){
                    setIsLogin(true);
                }else{
                    setIsLogin(false);
                }
            })
            .catch((error) => {
                console.log("실패");
            });
    }

    const logout= () => {
        callApi("/api/v1/logout", "post")
            .then((response)=>{
                setIsLogin(false);
                localStorage.clear();
                })
            .catch((e) => {
                console.log(e);
            });
    }

    return (
    <Fragment>
    <CssBaseline/>
    <AppBar position="fixed">
        <Toolbar>
                    <IconButton
                        size="medium"
                        edge="start"
                        color="inherit"
                        aria-label="menu"
                        onClick={()=>{setSideMenuFlag(true)}}
                    >
                        <MenuRounded/>
                    </IconButton>
                    <Typography variant="h6"
                                component="h1" noWrap
                                className={classes.typographyStyles}
                                align="center"
                                onClick={()=>{window.location.href="/"}}
                                style={{cursor:"pointer"}}
                    >
                        미니미
                    </Typography>

                <IconButton
                    size="medium"
                aria-label="current-user"
                aria-controls="menu-appbar"
                aria-haspopup={true}
                color="inherit"
                onClick={postButtonAction}
                >
                    <Tooltip title="글쓰기" placement="top">
                        <Edit/>
                   </Tooltip>
                </IconButton>
            {isLogin ?
                <IconButton
                    size="medium"
                    aria-label="current-user"
                    aria-controls="menu-appbar"
                    aria-haspopup={true}
                    color="inherit"
                    onClick={logout}

                >
                {/*{localStorage.getItem("name")}({localStorage.getItem("nickName")})*/}
                    <Tooltip title="로그아웃" placement="top"><VpnKey/></Tooltip>
                </IconButton>
                :
                <IconButton
                    size="medium"
                    aria-label="current-user"
                    aria-controls="menu-appbar"
                    color="inherit"
                    onClick={()=>{window.location.href="/login"}}
                >
                <Tooltip title="로그인" placement="top"><Input/></Tooltip>
                </IconButton>
                    }

        </Toolbar>
    </AppBar>
        <Drawer
            open={sideMenuFlag}
            onClose={()=>{setSideMenuFlag(false)}}
        >
            <SideMenu setIsLoginModalOpen={setIsLoginModalOpen}/>
        </Drawer>
      <Dialog open={isLoginModalOpen}
        onClose={()=>{setIsLoginModalOpen(false)}}
      >
        <DialogTitle>
            {"로그인 후 이용가능합니다"}
        </DialogTitle>
        <DialogContent>
            <DialogContentText>
                로그인 하시겠습니까?
            </DialogContentText>
        </DialogContent>
        <DialogActions>
            <Button onClick={()=>{setIsLoginModalOpen(false)}}>아니오</Button>
            <Button onClick={()=>{window.location.href="/login"}}>로그인</Button>
        </DialogActions>
      </Dialog>
    </Fragment>
)}

export default Header;