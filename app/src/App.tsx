import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Registro from "./pages/register/Registro";
import Login from "./pages/login/Log";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/register" element={<Registro />} />
        <Route path="/login" element={<Login></Login>}></Route>

        <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
