import React from "react";
import user from "../api/user";

class Chat extends React.Component {

    constructor(props, context) {
        super(props);
        this.state = {messageContent: '', selectedOption: 'ALL', messageSubject: ''};

        if (localStorage.getItem('isLogin') === 'FALSE') {
            this.props.history.push('/forbidden')
        }
    }

    componentDidMount = async () => {

        if (this.props.registeredUsers == null && this.props.loggedInUsers == null) {
            console.log("SAlhiiii");
            await user.get('/users/registered/' + localStorage.getItem('sessionId'));
            await user.get('/users/loggedIn/' + localStorage.getItem('sessionId'));

        }

        if (this.props.messages == null) {
            await user.get('/messages/' + localStorage.getItem('sessionId'));
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

    onChangeMessage = (event) => {
        this.setState({'messageContent': event.target.value});
    }

    onSendClick = async (event) => {
        event.preventDefault();

        if(this.state.messageContent.trim() === '' || this.state.messageSubject.trim() === '') {
            alert("Incorrect message!");
            return;
        }

        if (this.state.selectedOption === 'ALL') {
            console.log("salji svima");
            await user.post('/messages/all/' + localStorage.getItem('sessionId') + '/' + this.state.messageSubject + '/' + this.state.messageContent);
        } else {
            await user.post('/messages/user/' + localStorage.getItem('sessionId') + '/' + this.state.selectedOption + '/'+ this.state.messageSubject + '/' + this.state.messageContent);

        }



        console.log(this.state.messageContent);
        console.log(this.state.selectedOption);
        console.log(this.state.messageSubject);
    }

    onChangeOptions = (event) => {
        console.log(event.target.value)
        this.setState({'selectedOption': event.target.value});
    }

    onChangeSubject = (event) => {
        this.setState({'messageSubject': event.target.value});
    }

    renderOptions = () => {
        if (this.props.loggedInUsers !== null) {
            return (
                this.props.loggedInUsers.map((item) => {
                    return (
                        <option value={item}>{item}</option>
                    )
                })
            );
        }
    }

    renderMessages = () => {
        if (this.props.messages != null) {
            return (

                this.props.messages.map((item) => {
                    return (
                        <div>

                            <div className={`border border-dark rounded p-3`}>
                                <div className={`row`} >
                                    <div className={`col-6`}>
                                        <div>
                                            <label>Sender: {item.sender}</label>
                                        </div>
                                        <div>
                                            <label>Receiver: {item.receiver}</label>
                                        </div>
                                    </div>

                                    <div className={`col-6`}>
                                        <label>Time: {item.time}</label>
                                        <label>Subject: {item.subject}</label>
                                    </div>
                                </div>

                                <div>
                                    Content:
                                </div>
                                <div>
                                    {item.content}
                                </div>

                            </div>
                        </div>

                    );


                })

            )

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
                        <div className={`col-6 `}>
                            <h5 className={`d-flex justify-content-center`}>Registered users:</h5>
                            <div className={`border border-dark rounded p-3`}>
                                {this.renderRegisteredUsers()}

                            </div>

                        </div>

                        <div className={`col-6 `}>

                            <h5 className={`d-flex justify-content-center`}>Logged users:</h5>
                            <div className={`border border-dark rounded p-3`}>
                                {this.renderLoggedUsers()}

                            </div>
                        </div>


                    </div>
                    <div className={`row mt-3 `}>
                        <div className={`col-6`}>
                            <h5 className={`d-flex justify-content-center`}>Messages:</h5>
                            {this.renderMessages()}
                        </div>

                        <div className={`col-6 `}>
                            <h5 className={`d-flex justify-content-center`}>Send:</h5>

                            <form className={`border border-dark rounded p-3`}>
                                <div className={`form-group`}>
                                    <label >Subject:</label>
                                    <input className={`w-100`} type={`text`} onChange={this.onChangeSubject} value={this.state.messageSubject}/>

                                </div>

                                <div className={`form-group`}>
                                    <label >Content:</label>
                                    <input className={`w-100`} type={`text`} onChange={this.onChangeMessage} value={this.state.messageContent}/>

                                </div>

                                <div className={`form-group`}>
                                    <label>To:</label>
                                    <select className={`custom-select`} onChange={this.onChangeOptions}>
                                        <option value="ALL" >ALL</option>
                                        {this.renderOptions()}
                                </select>

                                </div>

                                <div className={`d-flex justify-content-center mb-3`}>
                                    <button className={`btn btn-dark w-50`} onClick={this.onSendClick}>Send</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            );
    }
}

export default Chat;