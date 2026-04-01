import { useState } from "react";

const Home = () => {
  const userId = localStorage.getItem("userId");
  const email = localStorage.getItem("email");

  const [showModal, setShowModal] = useState(false);
  const [curpInput, setCurpInput] = useState("");
  const [perfil, setPerfil] = useState<any>(null);

  const handlePerfilClick = () => {
    setShowModal(true);
  };

  const handleVerifyCurp = async () => {
    try {
      console.log("USER ID:", userId);

      const response = await fetch("http://localhost:8080/user/profile", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          userId: Number(userId),
          curp: curpInput,
        }),
      });

      if (!response.ok) throw new Error("CURP incorrecta");

      const data = await response.json();

      setPerfil(data);
      setShowModal(false);
    } catch (error) {
      alert("CURP incorrecta");
    }
  };

  return (
    <div>
      {/* HEADER */}
      <div className="header">
        <h2>Bienvenido, {email || "Usuario"} 👋</h2>

        <div className="profileIcon" onClick={handlePerfilClick}>
          👤
        </div>
      </div>

      {/* PERFIL */}
      {perfil && (
        <div className="perfilCard">
          <h3>Perfil</h3>
          <p>
            <strong>Nombre:</strong> {perfil.nombre}
          </p>
          <p>
            <strong>Email:</strong> {perfil.email}
          </p>
          <p>
            <strong>CURP:</strong> {perfil.curp}
          </p>
        </div>
      )}

      {/* MODAL */}
      {showModal && (
        <div className="modalOverlay">
          <div className="modal">
            <h3>Verificar identidad</h3>
            <p>Ingresa tu CURP</p>

            <input
              type="text"
              value={curpInput}
              onChange={(e) => setCurpInput(e.target.value)}
              placeholder="CURP"
            />

            <button onClick={handleVerifyCurp}>Validar</button>
            <button onClick={() => setShowModal(false)}>Cancelar</button>
          </div>
        </div>
      )}

      <style>{`
        .header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          padding: 20px 40px;
          background: #1f4037;
          color: white;
        }

        .profileIcon {
          font-size: 24px;
          cursor: pointer;
        }

        .perfilCard {
          margin: 40px;
          padding: 20px;
          background: white;
          border-radius: 12px;
          box-shadow: 0 10px 25px rgba(0,0,0,0.1);
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
          border-radius: 12px;
          width: 300px;
          display: flex;
          flex-direction: column;
          gap: 10px;
        }

        input {
          padding: 10px;
          border-radius: 8px;
          border: 1px solid #ccc;
        }

        button {
          padding: 10px;
          border: none;
          border-radius: 8px;
          background: #1f4037;
          color: white;
          cursor: pointer;
        }
      `}</style>
    </div>
  );
};

export default Home;
