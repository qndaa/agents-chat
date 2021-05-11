import React from "react";

class Chat extends React.Component {

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
                        <button className={`btn btn-dark `}>Log out</button>
                    </div>
                </div>


                <div className={`row`}>
                    <div className={`col-3`}>
                       <p>Active users:</p>
                    </div>

                    <div className={`col-9`}>
                       <p>Messages:</p>
                    </div>
              </div>

          </div>


        );





    }


}

export default Chat;