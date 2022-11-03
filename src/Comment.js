import {Avatar, Button, Grid, Paper, Typography} from "@material-ui/core";

const Comment = (props)=>{
    const {comment} = props;
    return(
        <Paper style={{padding:"40px 20px",marginTop:10}}>
            <Grid container wrap="nowrap" spacing={2}>
                <Grid item>
                    <Avatar>{comment.nickName}</Avatar>
                </Grid>
                <Grid  item xs zeroMinWidth>
                    <Typography variant="h4">
                        {comment.nickName}
                    </Typography>
                    <Typography variant="body1" style={{textAlign:"left"}}>
                        {comment.content}
                    </Typography>
                    <Button color="primary">답글</Button>
                </Grid>
            </Grid>
        </Paper>
    );
}

export default Comment;