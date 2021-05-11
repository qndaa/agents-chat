import React from "react";
import { BrowserRouter, Route} from "react-router-dom";
import Home from "./Home";
import Chat from "./Chat";
import Forbidden from "./Forbidden";

class App extends React.Component {


    constructor(props) {
        super(props);
        let sessionId;
        if (localStorage.getItem('sessionId') == null) {
            sessionId = '';
        } else {
            sessionId = localStorage.getItem('sessionId');
        }
        const url = "ws://localhost:8080/ChatWAR/ws/" + sessionId;
        let socket;


        try{
            socket = new WebSocket(url);
            console.log('connect: Socket Status: '+socket.readyState);

            socket.onopen = function(){
                console.log('onopen: Socket Status: '+socket.readyState+' (open)');
            }

            socket.onmessage = function(msg){
                console.log('onmessage: Received: '+ msg.data);
                sessionId = msg.data.split(":")[1];
                localStorage.setItem('sessionId', sessionId);
                console.log('Id saved in local storage - id:' + sessionId);
            }

            socket.onclose = function(){
                socket = null;
                localStorage.removeItem('sessionId');
            }

        } catch(exception){
            console.log('Error'+ exception);
        }
    }


    state = {isLogin: false}



    componentDidMount() {
        this.setState({isLogin: this.checkLogin});
    }

    checkLogin = () => {
        return true;
    }


    render() {
        return (
            <div className={`container`}>
                <BrowserRouter>
                    <div>
                        <Route path={`/`} exact={true} component={Home}/>
                        <Route path={`/home`} exact={true} component={Home}/>
                        <Route path={`/chat`} exact={true} component={Chat} />
                        <Route path={'/forbidden'} exaxt={true} component={Forbidden} />
                    </div>
                </BrowserRouter>
            </div>
        )

    }


}
export default App;