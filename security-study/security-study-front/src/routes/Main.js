import { Route, Routes } from "react-router-dom";

import Home from "../pages/Home";
import Login from "../pages/Login";
import Join from "../pages/Join";
import LoginSuccess from "../pages/LoginSuccess";

const Main = () => {

    return (
        <div>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/login" element={<Login />} />
                <Route path="/join" element={<Join />} />
                <Route path="/success" element={<LoginSuccess />} />
            </Routes>
        </div>
    )

}

export default Main;