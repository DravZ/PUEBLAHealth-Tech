import { useState } from "react";
import { Link } from "react-router-dom";

const Login = () => {
  const [showOtpModal, setShowOtpModal] = useState(false);
  const [otp, setOtp] = useState("");
  const [loadingOtp, setLoadingOtp] = useState(false);
  const [form, setForm] = useState({
    email: "",
    password: "",
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const response = await fetch("http://localhost:8080/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          email: form.email,
          password: form.password,
        }),
      });

      if (!response.ok) {
        throw new Error("Credenciales incorrectas");
      }

      const data = await response.json();

      console.log("OTP enviado:", data);

      // Abrimos el modal para validar OTP
      setShowOtpModal(true);
    } catch (error) {
      console.error("Error:", error);
      alert("Error al iniciar sesión");
    }
  };

  const handleVerifyOtp = async () => {
    setLoadingOtp(true);

    try {
      const response = await fetch("http://localhost:8080/auth/verify-otp", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          email: form.email,
          otp: otp,
        }),
      });

      if (!response.ok) {
        throw new Error("Código incorrecto");
      }

      const data = await response.json();

      console.log("OTP verificado:", data);

      if (data.token) {
        localStorage.setItem("token", data.token);
      }

      setShowOtpModal(false);

      alert("Bienvenido 🎉");
    } catch (error) {
      console.error(error);
      alert("Código OTP incorrecto");
    } finally {
      setLoadingOtp(false);
    }
  };

  return (
    <div className="container">
      <div className="card">
        <h1>Iniciar Sesión</h1>
        <p className="subtitle">Acceso para personal médico</p>

        <form onSubmit={handleSubmit}>
          <div className="inputGroup">
            <label>Correo electrónico</label>
            <input
              type="email"
              name="email"
              placeholder="ejemplo@hospital.com"
              value={form.email}
              onChange={handleChange}
              required
            />
          </div>

          <div className="inputGroup">
            <label>Contraseña</label>
            <input
              type="password"
              name="password"
              placeholder="••••••••"
              value={form.password}
              onChange={handleChange}
              required
            />
          </div>
          <div style={{ textAlign: "center", marginTop: "10px" }}>
            <Link
              to="/register"
              style={{
                color: "#1f4037",
                fontSize: "14px",
                textDecoration: "none",
                fontWeight: "500",
              }}
            >
              ¿No tienes cuenta? Regístrate
            </Link>
          </div>
          <button type="submit">Ingresar</button>
        </form>
        {showOtpModal && (
          <div className="modalOverlay">
            <div className="modal">
              <h2>Verificación</h2>
              <p>Ingresa el código enviado a tu correo</p>

              <input
                type="text"
                placeholder="Código OTP"
                value={otp}
                onChange={(e) => setOtp(e.target.value)}
              />

              <button onClick={handleVerifyOtp} disabled={loadingOtp}>
                {loadingOtp ? "Verificando..." : "Verificar"}
              </button>
            </div>
          </div>
        )}
      </div>

      <style>{`
        .container {
          height: 98vh;
          display: flex;
          justify-content: center;
          align-items: center;
          background: linear-gradient(135deg, #1f4037, #99f2c8);
          font-family: 'Segoe UI', sans-serif;
        }

        .card {
          background: white;
          padding: 45px;
          width: 380px;
          border-radius: 18px;
          box-shadow: 0 20px 40px rgba(0,0,0,0.15);
          animation: fadeIn 0.5s ease-in-out;
        }

        h1 {
          margin: 0;
          font-size: 26px;
          color: #1f4037;
        }

        .subtitle {
          margin-bottom: 25px;
          color: #777;
          font-size: 14px;
        }

        .inputGroup {
          display: flex;
          flex-direction: column;
          margin-bottom: 18px;
        }

        label {
          font-size: 13px;
          margin-bottom: 6px;
          color: #444;
        }

        input {
          padding: 12px;
          border-radius: 10px;
          border: 1px solid #ddd;
          font-size: 14px;
          transition: all 0.2s ease;
        }

        input:focus {
          outline: none;
          border-color: #1f4037;
          box-shadow: 0 0 0 3px rgba(31,64,55,0.15);
        }

        button {
          width: 100%;
          padding: 14px;
          margin-top: 10px;
          border: none;
          border-radius: 12px;
          background: #1f4037;
          color: white;
          font-weight: bold;
          font-size: 15px;
          cursor: pointer;
          transition: 0.3s ease;
        }

        button:hover {
          background: #163029;
          transform: translateY(-2px);
        }

        @keyframes fadeIn {
          from { opacity: 0; transform: translateY(10px); }
          to { opacity: 1; transform: translateY(0); }
        }

        .modalOverlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0,0,0,0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal {
  background: white;
  padding: 30px;
  border-radius: 15px;
  width: 320px;
  text-align: center;
  animation: fadeIn 0.3s ease;
}

.modal input {
  width: 100%;
  padding: 12px;
  margin-top: 15px;
  border-radius: 10px;
  border: 1px solid #ddd;
}

.modal button {
  margin-top: 15px;
  width: 100%;
}
      `}</style>
    </div>
  );
};

export default Login;
