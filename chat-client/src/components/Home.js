import React from "react";
import user from "../api/user";

class Home extends React.Component {

    state = {username: '', password: ''}

    onRegistrationClick = async (event) => {
        event.preventDefault();
        console.log("Registration: username:" + this.state.username + " password: " + this.state.password);

        const response = await user.post('/users/registration/' + localStorage.getItem('sessionId'),
            {username : this.state.username, password: this.state.password});

        console.log(response);
    }

    onUsernameChange = (event) => {
        this.setState( { username: event.target.value });
    }

    onPasswordChange = (event) => {
        this.setState({ password: event.target.value });
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
                            <button type="submit" className={`btn btn-primary w-25 mr-3`}>Login</button>
                            <button type="submit" className={`btn btn-primary w-25 ml-3`} onClick={this.onRegistrationClick}>Registration</button>
                        </div>
                    </form>

                </div>
            </div>
        );
    }
}

export default Home;