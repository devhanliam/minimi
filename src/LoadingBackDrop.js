import {Backdrop, CircularProgress} from "@material-ui/core";

const LoadingBackDrop = (props) => {
    return (
        <Backdrop open={props.open}
                  style={{color: '#fff',
                        zIndex: '999'

                  }}
        >
            <CircularProgress color="primary"/>
        </Backdrop>
    );
}
export default LoadingBackDrop;