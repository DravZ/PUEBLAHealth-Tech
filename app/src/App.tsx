import { BrowserRouter, Routes, Route } from "react-router-dom";
import Registro from "./pages/register/registro";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/register" element={<Registro />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
