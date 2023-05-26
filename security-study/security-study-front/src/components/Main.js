import { Route, Routes } from "react-router-dom";
import Home from "./Home";
import Login from "./Login";
import Join from "./Join";

const Main = () => {


    return (
        <>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/login" element={<Login />} />
                <Route path="/join" element={<Join />} />
            </Routes>

        </>
    )

}

export default Main;