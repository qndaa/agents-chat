import React from "react";

class Forbidden extends React.Component {

    onClickButton = () => {
        window.location.href = '/';
    }
    render() {
        return (
          <div className={`m-5`}>
              <h2 className={`text-dark`}>Forbidden error!</h2>
              <button className={`btn btn-dark`} onClick={this.onClickButton}>Go on Home page</button>
          </div>
        );
    }
}

export default Forbidden;