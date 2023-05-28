import { Route, Routes } from "react-router-dom";
import Home from "../pages/Home";
import Login from "../pages/Login";
import Join from "../pages/Join";
import LoginSuccess from "../pages/LoginSuccess";

const Main = () => {

    return (
        <>
            <Routes>
                <Route path="/" element={<Login />} />
                {/* <Route path="/login" element={<Login />} /> */}
                <Route path="/join" element={<Join />} />
                <Route path="/success" element={<LoginSuccess />} />
                <Route path="/logout" element={<Home />} />
            </Routes>

        </>
    )

}

export default Main;