import './App.css';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { Login } from './Components/Landing/Login';
import { Register } from './Components/Landing/Register';
import { Dashboard } from './Components/Dashboard/Dashboard';

function App() {
    return (
        <div className="App">
            <BrowserRouter>
                <Routes>
                    <Route path="" element={<Login />} />
                    <Route path="/register" element={<Register />} />
                    <Route path="/dashboard" element={<Dashboard />} />
                </Routes>
            </BrowserRouter>
        </div>
    );
}

export default App;
