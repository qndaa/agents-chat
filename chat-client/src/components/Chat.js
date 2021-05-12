import React from "react";
import user from "../api/user";

class Chat extends React.Component {

    constructor(props, context) {
        super(props);
        if (localStorage.getItem('isLogin') === 'FALSE') {
            this.props.history.push('/forbidden')
        }
    }

    componentDidMount = async () => {

        if (this.props.registeredUsers == null && this.props.loggedInUsers == null) {
            await user.get('/users/registered/' + localStorage.getItem('sessionId'));
            await user.get('/users/loggedIn/' + localStorage.getItem('sessionId'));

        }


    }

    logout = async () => {
        await user.delete('/users/loggedIn/' + localStorage.getItem('sessionId') + '/' + localStorage.getItem('username'));
        localStorage.setItem('isLogin', 'FALSE');
        localStorage.removeItem('username');

        await this.sleep(300);
        this.props.history.push('/home');
    }

    sleep = (ms) => {
        return new Promise(resolve => setTimeout(resolve, ms));
    }

    renderRegisteredUsers() {
        if (this.props.registeredUsers == null) {
            return (
                <div>
                    <p>Loading...</p>
                </div>

            )
        } else {
            return (
                this.props.registeredUsers.map((item) => {
                    return (
                        <div className={`d-flex justify-content-center`}>
                            <p>{item}</p>
                        </div>
                    );
                })
            );
        }

    }

    renderLoggedUsers = () => {
        if (this.props.loggedInUsers == null) {
            return (
                <div>
                    <p>Loading...</p>
                </div>

            )
        } else {
            return this.props.loggedInUsers.map((item) => {
                return (
                    <div className={`d-flex justify-content-center`}>
                        <p>{item}</p>
                    </div>
                );

            })

        }



    }

    render() {
            return (
                <div className={`mt-5 `}>
                    <div className={`row mb-5`}>
                        <div className={`col-4`}>

                        </div>
                        <div className={`col-4`}>
                            <h2 className={`d-flex justify-content-center font-weight-bold`}>Welcome to Chat!</h2>
                        </div>
                        <div className={`col-4 d-flex justify-content-end`}>
                            <button className={`btn btn-dark `} onClick={this.logout}>Log out</button>
                        </div>
                    </div>


                    <div className={`row`}>
                        <div className={`col-3 border border-dark rounded`}>
                            <p className={`d-flex justify-content-center`}>Registered users:</p>
                            {this.renderRegisteredUsers()}

                        </div>

                        <div className={`col-3 border border-dark rounded`}>
                            <p className={`d-flex justify-content-center`}>Logged users:</p>
                            {this.renderLoggedUsers()}
                        </div>
                    </div>

                </div>
            );
    }
}

export default Chat;