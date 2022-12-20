import {Fragment, useState} from "react";
import {Box, Divider, Drawer, IconButton, List, ListItem, ListItemIcon, ListItemText} from "@material-ui/core";
import {AccountCircle, Home, ListAlt} from "@material-ui/icons";

const SideMenu = (props) => {
    let isLogin = localStorage.getItem("email") ? true : false;
    const myInfoClick = () => {
        if (isLogin) {
            window.location.href = "/user/info"
        }else{
            props.setIsLoginModalOpen(true);
        }

    }

    const myPostClick = () => {
        if (isLogin) {
            window.location.href = "/post/my/list"
        }else{
            props.setIsLoginModalOpen(true);
        }

    }
 return(
     <Box
         style={{width:250}}
         role="presentation"
     >
         <List>
             <ListItem>
                 <IconButton onClick={()=>{window.location.href="/"}}>
                     <ListItemIcon>
                         <Home/>
                     </ListItemIcon>
                     <ListItemText primary="홈"/>
                 </IconButton>
             </ListItem>
             <Divider/>
             <ListItem>
                 <IconButton onClick={myInfoClick}>
                     <ListItemIcon>
                         <AccountCircle/>
                     </ListItemIcon>
                     <ListItemText primary="내정보"/>
                 </IconButton>
             </ListItem>
             <ListItem>
                 <IconButton onClick={myPostClick}>
                     <ListItemIcon>
                         <ListAlt/>
                     </ListItemIcon>
                     <ListItemText primary="내가쓴글"/>
                 </IconButton>
             </ListItem>
         </List>
     </Box>
 )
}
export default SideMenu;