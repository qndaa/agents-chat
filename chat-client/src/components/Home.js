import React from "react";
import user from "../api/user";

class Home extends React.Component {

    state = {username: '', password: ''}

    onRegistrationClick = async (event) => {
        event.preventDefault();
        console.log("Registration: username:" + this.state.username + " password: " + this.state.password);

        await user.post('/users/registration/' + localStorage.getItem('sessionId'),
            {username : this.state.username, password: this.state.password});

        this.setState( { username: '' });
        this.setState( { password: '' });
    }

    onLoginClick = async (event) => {
        event.preventDefault();
        console.log("Login: username:" + this.state.username + " password: " + this.state.password);


        await user.post('/users/login/' + localStorage.getItem('sessionId'),
           {username: this.state.username, password: this.state.password});

        await this.sleep(300);
        if (localStorage.getItem('isLogin') === 'TRUE') {
            this.props.history.push('/chat');
        }

    }

    sleep = (ms) => {
        return new Promise(resolve => setTimeout(resolve, ms));
    }

    onUsernameChange = (event) => {
        this.setState( { username: event.target.value });
    }

    onPasswordChange = (event) => {
        this.setState({ password: event.target.value });
    }

    componentDidMount() {

        if (localStorage.getItem('isLogin') === 'TRUE') {
            this.props.history.push('/chat')
        }
    }

    render() {
        return (
            <div className={`mt-5`}>
                <h1 className={`text-dark d-flex justify-content-center m-4 font-weight-bold`}>Chat App</h1>
                <div className={`d-flex justify-content-center`}>
                    <form className={`w-50`}>
                        <div className="form-group">
                            <label htmlFor="username">Username</label>
                            <input type="text" className="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="Enter username" value={this.state.username} onChange={this.onUsernameChange}/>
                        </div>
                        <div className="form-group">
                            <label htmlFor="exampleInputPassword1">Password</label>
                            <input type="password" className="form-control" id="exampleInputPassword1" placeholder="Password" value={this.state.password} onChange={this.onPasswordChange}/>
                        </div>
                        <div className={`d-flex justify-content-center`}>
                            <button type="submit" className={`btn btn-primary w-25 mr-3`} onClick={this.onLoginClick}>Login</button>
                            <button type="submit" className={`btn btn-primary w-25 ml-3`} onClick={this.onRegistrationClick}>Registration</button>
                        </div>
                    </form>

                </div>
            </div>
        );
    }
}

export default Home;