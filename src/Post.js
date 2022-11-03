import {
    Card,
    CardActionArea,
    CardContent, CardHeader,
    CardMedia,
    Grid,
    Typography
} from "@material-ui/core";
import PropTypes from 'prop-types';

function Post(props) {
    const {post} = props;
    return(
        <Grid item xs={12} md={4}>
            <CardActionArea onClick={()=>{window.location.href=`/post/${post.id}`}}>
                <Card sx={{display:"flex"}}>
                    <CardHeader
                        title={post.writer}
                        subheader={post.date}
                    />
                    <CardMedia
                        component="img"
                        sx={{
                            // height:80,
                            // display : {
                            //     xs:'none',
                            //     sm:'block'
                            // }
                        }}
                        style={{
                            maxHeight:500
                        }}

                        // image={post.image}
                        src={post.image}
                        alt={post.imageLabel}
                        onError={(event)=>{event.target.style.display='none'}}
                    >
                    </CardMedia>
                    <CardContent>
                        <Typography component="h2" variant="h5">
                            {post.title}
                        </Typography>
                        {/*<Viewer*/}
                        {/*    initialValue={post.content}*/}
                        {/*/>*/}
                        <Typography variant="body1"
                                    dangerouslySetInnerHTML={{__html:post.content}}
                                    style={{
                                        overflow : "hidden"
                                        ,whiteSpace : "nowrap"
                                        ,textOverflow : "ellipsis"
                                        ,height : 150
                        }}
                        />
                        <Typography variant="subtitle1" color="textSecondary">
                            조회수 : {post.views}
                        </Typography>
                    </CardContent>
                </Card>
            </CardActionArea>
        </Grid>
    )
}

Post.propTypes={
    post: PropTypes.shape({
        date: PropTypes.string.isRequired,
        content: PropTypes.string.isRequired,
        title: PropTypes.string.isRequired,
        image: PropTypes.string.isRequired,
        imageLabel: PropTypes.string.isRequired,
        writer: PropTypes.string.isRequired,
        id: PropTypes.number.isRequired,
        views:PropTypes.number.isRequired
    }).isRequired,
};

export default Post;

