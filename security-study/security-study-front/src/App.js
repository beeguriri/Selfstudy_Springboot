import { BrowserRouter } from "react-router-dom";
import Main from "./routes/Main";
import Login from "./pages/Login";


function App() {

    return (
      <>
        <div className="App">
          <BrowserRouter>
            <Main />
          </BrowserRouter>
        </div>
      </>
      
    );
  }

export default App;
