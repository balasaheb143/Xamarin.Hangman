using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SharedCode.Models
{
    public class Word
    {
        public int Id { get; set; }
        public string Text { get; set; }
        public string Hint { get; set; }
        public int DificultyType { get; set; }
    }
}
