import {Fragment, useState} from "react";
import {Box, Divider, Drawer, IconButton, List, ListItem, ListItemIcon, ListItemText} from "@material-ui/core";
import {AccountCircle, Home, ListAlt} from "@material-ui/icons";

const SideMenu = () => {
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
                 <IconButton>
                     <ListItemIcon>
                         <AccountCircle/>
                     </ListItemIcon>
                     <ListItemText primary="내정보"/>
                 </IconButton>
             </ListItem>
             <ListItem>
                 <IconButton>
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