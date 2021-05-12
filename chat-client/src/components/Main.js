import React from "react";
import {BrowserRouter, Route} from "react-router-dom";
import Home from "./Home";
import Chat from "./Chat";
import Forbidden from "./Forbidden";

class Main extends React.Component {



    constructor(props) {
        super(props);

        this.state = {registeredUsers: null, loggedInUsers: null}
    }

    componentDidMount = () => {


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
                localStorage.setItem('isLogin', 'FALSE');
            }

            socket.onmessage = (msg) => {

                console.log(msg.data);

                if (msg.data.startsWith("REGISTRATION")){
                    const tokens = msg.data.split(' ');
                    if (tokens[1] === 'REGISTRATION-SUCCESS') {
                        alert("Registration success!");
                    } else if (tokens[1] === 'REGISTRATION-FAILED') {
                        alert("Username already exists!");
                    }
                } else if (msg.data.startsWith('LOGIN')) {
                    const tokens = msg.data.split(' ');
                    if (tokens[1] === 'LOGIN-SUCCESS') {
                        localStorage.setItem('isLogin', 'TRUE');
                        localStorage.setItem('username', tokens[2]);
                    } else if (tokens[1] === 'LOGIN-FAILED') {
                        alert("Wrong username or password!");
                    }
                } else if (msg.data.startsWith('LOGOUT')) {
                    const tokens = msg.data.split(' ');
                    if (tokens[1] === 'LOGOUT-SUCCESS') {
                        //this.props.history.push('/home');
                    }
                } else if (msg.data.startsWith('REGISTERED_USERS')) {
                    const tokens = msg.data.split(' ');

                    this.setState({registeredUsers: tokens[2].split(',')});

                } else if (msg.data.startsWith('LOGGED_USERS')) {
                    const tokens = msg.data.split(' ');

                    this.setState({loggedInUsers: tokens[2].split(',')});

                }


                else {
                    console.log('onmessage: Received: '+ msg.data);
                    sessionId = msg.data.split(":")[1];
                    localStorage.setItem('sessionId', sessionId);
                    console.log('Id saved in local storage - id:' + sessionId);
                }
            }
            socket.onclose = function(){
                socket = null;
                localStorage.removeItem('sessionId');
                localStorage.removeItem('username');
                localStorage.setItem('isLogin', 'FALSE');
            }

        } catch(exception){
            console.log('Error'+ exception);
        }
    }




    render() {
        return (
            <div className={`container`}>
                <BrowserRouter>
                    <div>
                        <Route path={`/`} exact={true} component={Home}/>
                        <Route path={`/home`} exact={true} component={Home}/>
                        <Route path={`/chat`} exact={true} component={(props) => <Chat {...props} registeredUsers={this.state.registeredUsers} loggedInUsers={this.state.loggedInUsers} />} />
                        <Route path={'/forbidden'} exaxt={true} component={Forbidden} />
                    </div>
                </BrowserRouter>
            </div>
        )

    }
}

export default Main;